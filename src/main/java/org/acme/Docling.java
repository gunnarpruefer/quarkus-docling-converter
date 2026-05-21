package org.acme;

import ai.docling.serve.api.DoclingServeApi;
import ai.docling.serve.api.convert.request.ConvertDocumentRequest;
import ai.docling.serve.api.convert.request.options.ConvertDocumentOptions;
import ai.docling.serve.api.convert.request.options.OutputFormat;
import ai.docling.serve.api.convert.request.source.FileSource;
import ai.docling.serve.api.convert.request.source.HttpSource;
import ai.docling.serve.api.convert.response.ConvertDocumentResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.net.URI;
import java.util.Base64;

@ApplicationScoped
public class Docling {

    @Inject
    DoclingServeApi doclingServeApi;

    public ConvertDocumentResponse convertFromUrl(URI uri, OutputFormat outputFormat) {
        var source = HttpSource.builder()
                .url(uri)
                .build();

        var request = ConvertDocumentRequest.builder()
                .source(source)
                .options(ConvertDocumentOptions.builder().toFormat(outputFormat).build())
                .build();

        return doclingServeApi.convertSource(request);
    }

    public ConvertDocumentResponse convertFromBytes(byte[] content, String filename, OutputFormat outputFormat) {
        String base64 = Base64.getEncoder().encodeToString(content);
        return convertFromBase64(base64, filename, outputFormat);
    }

    public ConvertDocumentResponse convertFromBase64(String base64, String filename, OutputFormat outputFormat) {
        var source = FileSource.builder()
                .base64String(base64)
                .filename(filename)
                .build();

        var request = ConvertDocumentRequest.builder()
                .source(source)
                .options(ConvertDocumentOptions.builder().toFormat(outputFormat).build())
                .build();

        return doclingServeApi.convertSource(request);
    }
}
