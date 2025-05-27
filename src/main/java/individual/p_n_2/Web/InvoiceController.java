package individual.p_n_2.Web;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class InvoiceController {

    private static final String INVOICE_FOLDER_PATH = System.getProperty("user.home") + "/Desktop/faktury-testowe";

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadInvoice(@RequestParam String filename) throws MalformedURLException {
        Path filePath = Paths.get(INVOICE_FOLDER_PATH).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}