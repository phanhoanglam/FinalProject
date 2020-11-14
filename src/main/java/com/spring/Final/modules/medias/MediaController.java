package com.spring.Final.modules.medias;

import com.spring.Final.core.helpers.StorageService;
import com.spring.Final.core.infrastructure.ApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/medias")
public class MediaController extends ApiController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName, HttpServletRequest request) {
        String contentType = null;
        Resource resource = this.storageService.loadFileAsResource(fileName);

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ignored) {
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
