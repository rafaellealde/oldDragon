// service/AventuraService.kt
package com.olddragon.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Build
import androidx.core.app.NotificationCompat
import com.olddragon.MainActivity
import com.olddragon.model.Personagem
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class AventuraService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var personagem: Personagem? = null
    private var emAventura = false
    private var tempoUltimoCombate = 0L
    private var personagemMorto = false
    private var tempoMorte = 0L
    
    companion object {
        const val NOTIFICATION_ID = 123
        const val CHANNEL_ID = "aventura_channel"
        
        fun startService(context: Context, personagem: Personagem) {
            val intent = Intent(context, AventuraService::class.java).apply {
                putExtra("personagem", personagem)
            }
            context.startService(intent)
        }
        
        fun stopService(context: Context) {
            val intent = Intent(context, AventuraService::class.java)
            context.stopService(intent)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        personagem = intent?.getSerializableExtra("personagem") as? Personagem
        startAventura()
        
        // Criar notificação em primeiro plano
        val notification = createNotification("${personagem?.nome} está em aventura...")
        startForeground(NOTIFICATION_ID, notification)
        
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun startAventura() {
        emAventura = true
        serviceScope.launch {
            while (emAventura && personagem != null) {
                if (!personagemMorto) {
                    verificarCombate()
                } else {
                    verificarRecuperacao()
                }
                delay(1000) // Verifica a cada segundo
            }
        }
    }
    
    private suspend fun verificarCombate() {
        val agora = System.currentTimeMillis()
        val p = personagem ?: return
        
        // Calcular intervalo baseado no movimento (9m = 9 segundos)
        val intervaloCombate = TimeUnit.SECONDS.toMillis(p.raca.movimentoBase.toLong())
        
        if (agora - tempoUltimoCombate >= intervaloCombate) {
            iniciarCombate(p)
            tempoUltimoCombate = agora
        }
    }
    
    private suspend fun verificarRecuperacao() {
        val agora = System.currentTimeMillis()
        val tempoRecuperacao = TimeUnit.SECONDS.toMillis(30)
        
        if (agora - tempoMorte >= tempoRecuperacao) {
            personagemMorto = false
            personagem?.pvAtuais = personagem?.pvMaximos ?: 0
            
            // Notificar recuperação
            mostrarNotificacao(
                "Personagem Recuperado", 
                "${personagem?.nome} está pronto para nova aventura!"
            )
            updateNotification("${personagem?.nome} recuperado - em aventura")
        }
    }
    
    private suspend fun iniciarCombate(personagem: Personagem) {
        val combateService = CombateService()
        
        combateService.simularCombateAutomatico(
            personagem = personagem,
            onProgress = { log ->
                updateNotification(log)
            },
            onCombateTerminado = { resultado ->
                if (resultado.vitoria) {
                    // Vitória
                    personagem.experiencia += resultado.xpGanha
                    personagem.pvAtuais = maxOf(0, personagem.pvAtuais - resultado.danoSofrido)
                    
                    verificarSubidaNivel(personagem)
                    
                    updateNotification("${personagem.nome} venceu! +${resultado.xpGanha} XP")
                    
                    // Se morreu após vencer
                    if (personagem.pvAtuais <= 0) {
                        personagemMorto = true
                        tempoMorte = System.currentTimeMillis()
                        mostrarNotificacao(
                            "Personagem Morreu", 
                            "${personagem.nome} morreu após a vitória! Recuperando..."
                        )
                        updateNotification("${personagem.nome} morto - recuperando")
                    }
                    
                } else {
                    // Derrota
                    personagemMorto = true
                    tempoMorte = System.currentTimeMillis()
                    personagem.pvAtuais = 0
                    
                    mostrarNotificacao(
                        "Personagem Derrotado", 
                        "${personagem.nome} foi derrotado! Recuperando em 30 segundos..."
                    )
                    updateNotification("${personagem.nome} derrotado - recuperando")
                }
            }
        )
    }
    
    private fun verificarSubidaNivel(personagem: Personagem) {
        val xpProximoNivel = personagem.classe.xpParaProximoNivel(personagem.nivel)
        
        if (personagem.experiencia >= xpProximoNivel) {
            personagem.nivel++
            personagem.pvMaximos += personagem.classe.dadoVida
            personagem.pvAtuais = personagem.pvMaximos
            
            mostrarNotificacao(
                "Novo Nível!", 
                "${personagem.nome} alcançou o nível ${personagem.nivel}!"
            )
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Modo Aventura",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notificações do modo aventura"
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(text: String): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Old Dragon - Aventura")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .build()
    }
    
    private fun updateNotification(text: String) {
        val notification = createNotification(text)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    private fun mostrarNotificacao(titulo: String, mensagem: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(titulo)
            .setContentText(mensagem)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        emAventura = false
    }
}