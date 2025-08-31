package org.acme;

import io.quarkiverse.docling.runtime.client.api.DoclingApi;
import io.quarkiverse.docling.runtime.client.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.net.URI;
import java.util.Base64;
import java.util.List;

@ApplicationScoped
public class Docling {

    @Inject
    DoclingApi doclingApi;

    public ConvertDocumentResponse convertFromUrl(URI uri, OutputFormat outputFormat) {
        HttpSource source = new HttpSource();
        source.setUrl(uri);

        ConversionRequest request = new ConversionRequest()
                .addHttpSourcesItem(source)
                .options(new ConvertDocumentsOptions().toFormats(List.of(outputFormat)));

        return doclingApi.processUrlV1alphaConvertSourcePost(request);
    }

    public ConvertDocumentResponse convertFromBytes(byte[] content, String filename, OutputFormat outputFormat) {
        String base64 = Base64.getEncoder().encodeToString(content);
        return convertFromBase64ToText(base64, filename, outputFormat);
    }

    public ConvertDocumentResponse convertFromBase64ToText(String base64, String filename, OutputFormat outputFormat) {
        FileSource source = new FileSource()
                .base64String(base64)
                .filename(filename);

        ConversionRequest request = new ConversionRequest()
                .addFileSourcesItem(source)
                .options(new ConvertDocumentsOptions().toFormats(List.of(outputFormat)));

        return doclingApi.processUrlV1alphaConvertSourcePost(request);
    }
}
