package com.example.prog4.service;

import com.lowagie.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class PdfService {
    private TemplateEngine templateEngine;

    public byte[] toPdf(String eId, String lastName, String firstName, LocalDate entranceDate, LocalDate departureDate, String cnaps) throws IOException {
        Context context= new Context();
        context.setVariable("id", eId);
        context.setVariable("lastName", lastName);
        context.setVariable("firstName", firstName);
        context.setVariable("entranceDate", entranceDate);
        context.setVariable("departureDate", departureDate);
        context.setVariable("cnaps", cnaps);

        String procHTML= templateEngine.process("pdf-template", context);

        try(ByteArrayOutputStream outputStream= new ByteArrayOutputStream()){
            ITextRenderer render = new ITextRenderer();
            render.setDocumentFromString(procHTML);
            render.layout();
            render.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePdfFromHtml(String html, String outputFilePath) throws Exception {
        OutputStream outputStream = new FileOutputStream(outputFilePath);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
    }
}
