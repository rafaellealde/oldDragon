// service/AventuraService.kt
package com.olddragon.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.olddragon.model.Personagem
import com.olddragon.model.combate.ResultadoCombate
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
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "aventura_channel"
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        personagem = intent?.getSerializableExtra("personagem") as? Personagem
        startAventura()
        
        // Manter serviço rodando em primeiro plano
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Old Dragon - Modo Aventura")
            .setContentText("${personagem?.nome} está em aventura...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .build()
        
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
        val personagem = this.personagem ?: return
        
        // Calcular intervalo baseado no movimento
        val intervaloCombate = TimeUnit.SECONDS.toMillis(personagem.raca.movimentoBase.toLong())
        
        if (agora - tempoUltimoCombate >= intervaloCombate) {
            iniciarCombate(personagem)
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
            mostrarNotificacao("Old Dragon", "${personagem?.nome} se recuperou e está pronto para aventura!")
        }
    }
    
    private suspend fun iniciarCombate(personagemAtual: Personagem) {
        val combateService = CombateService()
        val inimigo = combateService.gerarInimigoAleatorio(personagemAtual.nivel)
        
        val resultado = combateService.simularCombate(personagemAtual, inimigo)
        
        if (resultado.vitoria) {
            // Vitória - ganhar XP
            personagemAtual.experiencia += resultado.xpGanha
            personagemAtual.pvAtuais -= resultado.danoSofrido
            
            // Verificar se subiu de nível
            verificarSubidaNivel(personagemAtual)
            
            // Atualizar notificação
            atualizarNotificacao("${personagemAtual.nome} derrotou ${inimigo.nome}! +${resultado.xpGanha} XP")
            
        } else {
            // Derrota - personagem morre
            personagemMorto = true
            tempoMorte = System.currentTimeMillis()
            personagemAtual.pvAtuais = 0
            
            // Notificar morte
            mostrarNotificacao("Old Dragon - Personagem Morreu", 
                "${personagemAtual.nome} foi derrotado por ${inimigo.nome}! Recuperando em 30 segundos...")
        }
        this.personagem = personagemAtual // Update the service's character state
    }
    
    private fun verificarSubidaNivel(personagemAtual: Personagem) {
        val xpProximoNivel = personagemAtual.classe.xpParaProximoNivel(personagemAtual.nivel)
        
        if (personagemAtual.experiencia >= xpProximoNivel) {
            personagemAtual.nivel++
            personagemAtual.pvMaximos += personagemAtual.classe.dadoVida
            personagemAtual.pvAtuais = personagemAtual.pvMaximos
            
            mostrarNotificacao("Old Dragon - Novo Nível!", 
                "${personagemAtual.nome} alcançou o nível ${personagemAtual.nivel}!")
        }
    }
    
    private fun atualizarNotificacao(mensagem: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Old Dragon - Modo Aventura")
            .setContentText(mensagem)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .build()
        
        startForeground(NOTIFICATION_ID, notification)
    }
    
    private fun mostrarNotificacao(titulo: String, mensagem: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(titulo)
            .setContentText(mensagem)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()
        
        // Em um app real, você usaria NotificationManager aqui
        // Para fins educacionais, vamos apenas logar
        println("NOTIFICAÇÃO: $titulo - $mensagem")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        emAventura = false
    }
}