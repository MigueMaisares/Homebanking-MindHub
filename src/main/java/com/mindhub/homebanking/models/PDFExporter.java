package com.mindhub.homebanking.models;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;

import javax.servlet.http.HttpServletResponse;

public class PDFExporter {
    private List<Transaction> transactionList;
    private Transaction transaction;
    private String number;
    private String firstName;
    private String lastName;

    public PDFExporter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        this.number = transactionList.get(0).getAccount().getNumber();
        this.firstName = transactionList.get(0).getAccount().getClient().getFirstName();
        this.lastName = transactionList.get(0).getAccount().getClient().getLastName();
    }
    public PDFExporter(Transaction transaction) {
        this.transaction = transaction;
        this.number = transaction.getAccount().getNumber();
        this.firstName = transaction.getAccount().getClient().getFirstName();
        this.lastName = transaction.getAccount().getClient().getLastName();
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.lightGray);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("DATE", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("DESCRIPTION", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("TYPE", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("AMOUNT", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("BALANCE", font));
        table.addCell(cell);
    }

    private void writeTableHeader2(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.lightGray);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("DATE", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("DESCRIPTION", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("TYPE", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("AMOUNT", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("BALANCE", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        Font fontData = FontFactory.getFont(FontFactory.COURIER);
        for (Transaction transaction : transactionList) {
            table.addCell(new PdfPCell(new Phrase(transaction.getDate().toLocalDate().toString(), fontData)));
            table.addCell(new PdfPCell(new Phrase(transaction.getDescription(), fontData)));
            table.addCell(new PdfPCell(new Phrase(transaction.getType().toString(), fontData)));
            table.addCell(new PdfPCell(new Phrase(transaction.getAmount().toString())));
            table.addCell(new PdfPCell(new Phrase(transaction.getBalance().toString())));
        }
    }

    private void writeTableData2(PdfPTable table) {
        Font fontData = FontFactory.getFont(FontFactory.COURIER);
        table.addCell(new PdfPCell(new Phrase(transaction.getId().toString(), fontData)));
        table.addCell(new PdfPCell(new Phrase(transaction.getDate().toLocalDate().toString(), fontData)));
        table.addCell(new PdfPCell(new Phrase(transaction.getDescription(), fontData)));
        table.addCell(new PdfPCell(new Phrase(transaction.getType().toString(), fontData)));
        table.addCell(new PdfPCell(new Phrase(transaction.getAmount().toString(), fontData)));
        table.addCell(new PdfPCell(new Phrase(transaction.getBalance().toString(), fontData)));
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Image logo =Image.getInstance("https://enlacesx.xoc.uam.mx/22/images/godofredo2.jpg");
        logo.setAbsolutePosition(40, 777);
        logo.scalePercent(5);

        Image logo2 =Image.getInstance("http://18.206.193.104/pluginfile.php/1/theme_moove/logo/1633350785/logo_mindhub_head.png");
        logo2.setAbsolutePosition(79, 777);
        logo2.scalePercent(13);

        document.add(logo);
        document.add(logo2);

        font.setSize(18);
        font.setColor(Color.black);

        Paragraph titulo = new Paragraph("MINDHUB BROTHERS BANK", font);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph nombreDocumento = new Paragraph("Statement of Account", font);
        nombreDocumento.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph nombreCliente = new Paragraph("Client: " + lastName + " " + firstName);
        nombreCliente.setSpacingBefore(25);
        Paragraph numeroCuenta = new Paragraph("Account number: " + number);
        LocalDate localDate = LocalDate.now();
        Paragraph fecha = new Paragraph("Date: " + localDate);

        document.add(titulo);
        document.add(nombreDocumento);
        document.add(nombreCliente);
        document.add(numeroCuenta);
        document.add(fecha);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.0f, 5.0f, 1.25f, 1.5f, 1.5f});
        table.setSpacingBefore(25);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();
    }

    public void export2(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Image logo =Image.getInstance("https://enlacesx.xoc.uam.mx/22/images/godofredo2.jpg");
        logo.setAbsolutePosition(40, 777);
        logo.scalePercent(5);

        Image logo2 =Image.getInstance("http://18.206.193.104/pluginfile.php/1/theme_moove/logo/1633350785/logo_mindhub_head.png");
        logo2.setAbsolutePosition(79, 777);
        logo2.scalePercent(13);

        document.add(logo);
        document.add(logo2);

        font.setSize(18);
        font.setColor(Color.black);

        Paragraph titulo = new Paragraph("MINDHUB BROTHERS BANK", font);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph nombreDocumento = new Paragraph("Transfer voucher", font);
        nombreDocumento.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph nombreCliente = new Paragraph("Client: " + lastName + " " + firstName);
        nombreCliente.setSpacingBefore(25);
        Paragraph numeroCuenta = new Paragraph("Account number: " + number);
        LocalDate localDate = LocalDate.now();
        Paragraph fecha = new Paragraph("Date: " + localDate);

        document.add(titulo);
        document.add(nombreDocumento);
        document.add(nombreCliente);
        document.add(numeroCuenta);
        document.add(fecha);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {0.6f, 2.0f, 5.0f, 1.25f, 2.0f, 2.0f});
        table.setSpacingBefore(25);

        writeTableHeader2(table);
        writeTableData2(table);

        document.add(table);

        document.close();
    }
}
