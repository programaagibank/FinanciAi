package org.financiai.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.financiai.model.entities.Financiamento;
import org.financiai.model.entities.Parcelas;
import org.financiai.model.entities.Cliente;
import org.financiai.model.entities.Imovel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GeradorPDF {

    public static void gerarPDF(Financiamento financiamento, Cliente cliente, Imovel imovel, List<Parcelas> parcelas) {
        Document document = new Document();

        try {
            // Cria o arquivo PDF
            String fileName = "SimulacaoFinanciamento_" + financiamento.getFinanciamentoId() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Cria uma tabela com 2 colunas: uma para o logo e outra para as informações
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setSpacingBefore(10);
            headerTable.setSpacingAfter(10);

            // Adiciona o logo na primeira coluna
            try {
                Image logo = Image.getInstance("C:\\Users\\rafae\\OneDrive\\Área de Trabalho\\FinanciAiFinal\\financiailogo.jpeg"); // Caminho do arquivo de logo
                logo.scaleToFit(200, 200); // Ajusta o tamanho do logo (largura, altura)

                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(Rectangle.NO_BORDER); // Remove a borda da célula
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Alinha o logo à esquerda
                headerTable.addCell(logoCell);
            } catch (IOException e) {
                System.err.println("Erro ao carregar o logo: " + e.getMessage());
            }

            // Adiciona as informações na segunda coluna
            PdfPCell infoCell = new PdfPCell();
            infoCell.setBorder(Rectangle.NO_BORDER); // Remove a borda da célula

            // Adiciona as informações do financiamento
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            Paragraph infoParagraph = new Paragraph();
            infoParagraph.add(new Paragraph("ID do Financiamento: " + financiamento.getFinanciamentoId(), normalFont));
            infoParagraph.add(new Paragraph("Cliente: " + cliente.getNome(), normalFont));
            infoParagraph.add(new Paragraph("Tipo de Imóvel: " + imovel.getTipoImovel(), normalFont));
            infoParagraph.add(new Paragraph("Valor do Imóvel: R$ " + String.format("%.2f", imovel.getValorImovel()), normalFont));
            infoParagraph.add(new Paragraph("Valor de Entrada: R$ " + String.format("%.2f", financiamento.getValorEntrada()), normalFont));
            infoParagraph.add(new Paragraph("Valor Financiado: R$ " + String.format("%.2f", financiamento.getValorFinanciado()), normalFont));
            infoParagraph.add(new Paragraph("Taxa de Juros: " + financiamento.getTaxaJuros() + "%", normalFont));
            infoParagraph.add(new Paragraph("Prazo: " + financiamento.getPrazo() + " meses", normalFont));
            infoParagraph.add(new Paragraph("Tipo de Amortização: " + financiamento.getTipoAmortizacao(), normalFont));
            infoParagraph.add(new Paragraph("Total a Pagar: R$ " + String.format("%.2f", financiamento.getTotalPagar()), normalFont));

            infoCell.addElement(infoParagraph);
            headerTable.addCell(infoCell);

            // Adiciona a tabela ao documento
            document.add(headerTable);

            // Adiciona título principal
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Simulação de Financiamento", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Adiciona tabela de parcelas
            document.add(new Paragraph("\nTabela de Parcelas:", headerFont));
            PdfPTable table = new PdfPTable(3); // 3 colunas: Parcela, Valor Parcela, Valor Amortização
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            // Cabeçalho da tabela
            table.addCell(new PdfPCell(new Phrase("Parcela", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Valor Parcela", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Valor Amortização", headerFont)));

            // Adiciona as parcelas à tabela
            for (Parcelas parcela : parcelas) {
                table.addCell(new PdfPCell(new Phrase("Parcela " + parcela.getNumeroParcela(), normalFont)));
                table.addCell(new PdfPCell(new Phrase("R$ " + String.format("%.2f", parcela.getValorParcela()), normalFont)));
                table.addCell(new PdfPCell(new Phrase("R$ " + String.format("%.2f", parcela.getValorAmortizacao()), normalFont)));
            }

            document.add(table);

            // Adiciona data de emissão
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            document.add(new Paragraph("\nData de Emissão: " + dateFormat.format(new Date()), normalFont));

            System.out.println("PDF gerado com sucesso: " + fileName);
        } catch (DocumentException | IOException e) {
            System.err.println("Erro ao gerar o PDF: " + e.getMessage());
        } finally {
            document.close();
        }
    }
}