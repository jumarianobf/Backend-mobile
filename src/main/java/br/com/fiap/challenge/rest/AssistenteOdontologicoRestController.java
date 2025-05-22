package br.com.fiap.challenge.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * REST Controller para interação via API com o assistente virtual odontológico
 * Projetado para ser consumido por aplicativos móveis React Native
 */
@RestController
@RequestMapping("/api/assistente-odontologico")
@Slf4j
public class AssistenteOdontologicoRestController {

    private final AzureOpenAiChatModel chatModel;

    private static final String SYSTEM_PROMPT =
            "Você é um assistente odontológico especializado da clínica ParrotTech. " +
                    "Ajude pacientes com dúvidas sobre saúde bucal, procedimentos dentários e cuidados preventivos. " +
                    "Suas respostas devem ser informativas, precisas e amigáveis, mas lembre o paciente que " +
                    "suas informações não substituem uma consulta profissional.";


    public AssistenteOdontologicoRestController(AzureOpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Endpoint para processar uma pergunta e retornar a resposta do assistente
     * Versão otimizada com timeout mais longo
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> processarPergunta(@RequestBody ChatRequest chatRequest) {
        log.info("Recebida pergunta: {}", chatRequest.getPergunta());

        try {
            // Validar pergunta
            if (chatRequest.getPergunta() == null || chatRequest.getPergunta().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new ChatResponse(
                                null,
                                chatRequest.getHistorico(),
                                "Por favor, digite uma pergunta."
                        )
                );
            }

            // Obter histórico ou inicializar se for nulo
            List<MensagemChat> historico = chatRequest.getHistorico();
            if (historico == null) {
                historico = new ArrayList<>();
            }

            log.info("Histórico recebido com {} mensagens", historico.size());

            // Adicionar pergunta ao histórico
            historico.add(new MensagemChat("usuario", chatRequest.getPergunta(), LocalDateTime.now()));

            // Prepara o prompt com todo o histórico para manter o contexto da conversa
            List<Message> messages = new ArrayList<>();
            messages.add(new SystemMessage(SYSTEM_PROMPT));

            // Adiciona o histórico para manter contexto da conversa
            for (MensagemChat msg : historico) {
                if ("usuario".equals(msg.getTipo())) {
                    messages.add(new UserMessage(msg.getConteudo()));
                } else {
                    messages.add(new AssistantMessage(msg.getConteudo()));
                }
            }

            // Cria o prompt com a lista de mensagens
            Prompt prompt = new Prompt(messages);

            log.info("Enviando prompt para o modelo AI");

            // Chama a API do Azure OpenAI com timeout mais longo
            CompletableFuture<org.springframework.ai.chat.model.ChatResponse> futureResponse =
                    CompletableFuture.supplyAsync(() -> chatModel.call(prompt));

            // Aguarda resposta com timeout de 60 segundos
            org.springframework.ai.chat.model.ChatResponse aiResponse =
                    futureResponse.get(60, TimeUnit.SECONDS);

            log.info("Resposta recebida do modelo AI");

            String conteudoResposta = aiResponse
                    .getResults()
                    .get(0)
                    .getOutput()
                    .getText();

            // Adiciona a resposta ao histórico
            historico.add(new MensagemChat("assistente", conteudoResposta, LocalDateTime.now()));

            log.info("Retornando resposta para o cliente");

            // Retorna a resposta e o histórico atualizado
            return ResponseEntity.ok(new ChatResponse(conteudoResposta, historico, null));

        } catch (Exception e) {
            log.error("Erro ao processar pergunta: {}", e.getMessage(), e);

            // Cria uma resposta de erro mais informativa
            String errorMessage = "Erro ao processar a solicitação";
            if (e instanceof java.util.concurrent.TimeoutException) {
                errorMessage = "O modelo de IA demorou muito para responder. Por favor, tente novamente.";
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ChatResponse(
                            "Desculpe, estou tendo dificuldades técnicas. Por favor, tente novamente mais tarde ou entre em contato com a clínica pelo telefone (11) 5555-1234.",
                            chatRequest.getHistorico(),
                            errorMessage + ": " + e.getMessage()
                    )
            );
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(status);
    }

    // Add this method to your controller for debugging
    @PostMapping("/debug")
    public ResponseEntity<Map<String, Object>> debugRequest(@RequestBody ChatRequest chatRequest) {
        Map<String, Object> debugInfo = new HashMap<>();

        try {
            debugInfo.put("receivedPergunta", chatRequest.getPergunta());
            debugInfo.put("historicoSize", chatRequest.getHistorico() != null ? chatRequest.getHistorico().size() : 0);
            debugInfo.put("timestamp", LocalDateTime.now().toString());

            // Test if we can access the AI model
            boolean aiModelAvailable = chatModel != null;
            debugInfo.put("aiModelAvailable", aiModelAvailable);

            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            debugInfo.put("error", e.getMessage());
            debugInfo.put("stackTrace", e.getStackTrace());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(debugInfo);
        }
    }    /**

    /**
     * Endpoint para obter informações sobre o assistente
     * @return Informações básicas sobre o assistente
     */
    @GetMapping("/info")
    public ResponseEntity<AssistenteInfo> getAssistenteInfo() {
        AssistenteInfo info = new AssistenteInfo(
                "Assistente Odontológico ParrotTech",
                "Assistente virtual especializado em saúde bucal",
                "1.0.0"
        );
        return ResponseEntity.ok(info);
    }

    /**
     * Classe para representar uma mensagem no chat
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MensagemChat implements Serializable {
        private static final long serialVersionUID = 1L;

        private String tipo; // "usuario" ou "assistente"
        private String conteudo;
        private LocalDateTime dataProcessamento;

        // Construtor adicional para compatibilidade com código existente
        public MensagemChat(String tipo, String conteudo) {
            this.tipo = tipo;
            this.conteudo = conteudo;
            this.dataProcessamento = LocalDateTime.now();
        }
    }

    /**
     * Classe para representar a requisição de chat
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRequest {
        private String pergunta;
        private List<MensagemChat> historico;
    }

    /**
     * Classe para representar a resposta do chat
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatResponse {
        private String resposta;
        private List<MensagemChat> historicoAtualizado;
        private String erro;
    }

    /**
     * Classe para representar informações sobre o assistente
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssistenteInfo {
        private String nome;
        private String descricao;
        private String versao;
    }


}