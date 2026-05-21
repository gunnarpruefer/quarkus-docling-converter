package org.acme;

import ai.docling.serve.api.convert.request.options.OutputFormat;
import ai.docling.serve.api.convert.response.ConvertDocumentResponse;
import ai.docling.serve.api.convert.response.InBodyConvertDocumentResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.nio.file.Files;

@Path("/convert")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.TEXT_PLAIN)
public class ConverterResource {

    @Inject
    Docling docling;

    @POST
    public Response convert(@RestForm("file") FileUpload file) {
        if (file == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: No file uploaded.").build();
        }

        try {
            byte[] imageBytes = Files.readAllBytes(file.uploadedFile());

            ConvertDocumentResponse result = docling.convertFromBytes(
                    imageBytes,
                    file.fileName(),
                    OutputFormat.TEXT);

            String textContent = ((InBodyConvertDocumentResponse) result).getDocument().getTextContent();
            return Response.ok(textContent).build();

        } catch (java.io.IOException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Failed to read uploaded file: " + e.getMessage())
                    .build();
        }
    }
}
