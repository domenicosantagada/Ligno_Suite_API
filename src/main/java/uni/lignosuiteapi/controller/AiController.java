package uni.lignosuiteapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    // Api key di Google Gemini
    @Value("${gemini.api.key}")
    private String GEMINI_API_KEY;

    // Endpoint per generare una descrizione migliorata a partire da un testo di input
    // @RequestBody Map<String, String> request: Riceve un JSON con una chiave "testo" che contiene la nota da migliorare
    // e restituisce un JSON con una chiave "descrizioneMigliorata" che contiene la descrizione migliorata
    @PostMapping("/genera-descrizione")
    public Map<String, String> generaDescrizione(@RequestBody Map<String, String> request) {
        String inputTesto = request.get("testo");

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prompt per la generazione della descrizione migliorata
        String prompt = "Sei un falegname esperto. L'utente ti darà una breve nota. " +
                "Trasformala in una descrizione professionale ed elegante da inserire in un preventivo. " +
                "Sii conciso ma tecnico. Evita asterischi o formattazioni markdown, scrivi solo testo normale. " +
                "Testo utente: " + inputTesto;

        // Struttura JSON richiesta da Gemini:
        // 1) text: il prompt
        // 2) parts: testo del prompt
        // 3) contents: testo generato
        Map<String, Object> textPart = Map.of("text", prompt);
        Map<String, Object> parts = Map.of("parts", List.of(textPart));
        Map<String, Object> body = Map.of("contents", List.of(parts));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> resParts = (List<Map<String, Object>>) content.get("parts");
            String testoGenerato = (String) resParts.get(0).get("text");

            Map<String, String> result = new HashMap<>();
            result.put("descrizioneMigliorata", testoGenerato.trim());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResult = new HashMap<>();
            errorResult.put("descrizioneMigliorata", "Errore nella generazione: " + inputTesto + " (Realizzazione artigianale su misura)");
            return errorResult;
        }
    }
}
