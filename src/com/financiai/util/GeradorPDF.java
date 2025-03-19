package com.financiai.util;



import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import model.entities.Parcelas;
import model.enums.TipoImovel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeradorPDF {

    public static void gerarPDF(int idFinanciamento, String nomeCliente, double valorTotalImovel, double valorEntrada, TipoImovel tipoImovel,
                                double valorFinanciado, double totalPagoPrice, double jurosTotaisPrice,
                                double totalPagoSAC, double jurosTotaisSAC, List<Parcelas> parcelas) {
        Document document = new Document();

        try {
            // Cria o arquivo PDF
            PdfWriter.getInstance(document, new FileOutputStream("SimulacaoFinanciamento_" + idFinanciamento + ".pdf"));
            document.open();

            // Adiciona conteúdo ao PDF
            document.add(new Paragraph("Resultado da Simulação"));
            document.add(new Paragraph("ID do Financiamento: " + idFinanciamento));
            document.add(new Paragraph("Cliente: " + nomeCliente));
            document.add(new Paragraph("Tipo de Imóvel: " + tipoImovel));
            document.add(new Paragraph("Valor do Imóvel: R$ " + String.format("%.2f", valorTotalImovel)));
            document.add(new Paragraph("Valor de Entrada: R$ " + String.format("%.2f", valorEntrada)));
            document.add(new Paragraph("Valor Financiado: R$ " + String.format("%.2f", valorFinanciado)));
            document.add(new Paragraph("\nSistema Price:"));
            document.add(new Paragraph("Total pago: R$ " + String.format("%.2f", totalPagoPrice)));
            document.add(new Paragraph("Juros totais: R$ " + String.format("%.2f", jurosTotaisPrice)));
            document.add(new Paragraph("\nSistema SAC:"));
            document.add(new Paragraph("Total pago: R$ " + String.format("%.2f", totalPagoSAC)));
            document.add(new Paragraph("Juros totais: R$ " + String.format("%.2f", jurosTotaisSAC)));
            document.add(new Paragraph("\nTabela de Parcelas:"));

            // Adiciona as parcelas ao PDF
            for (Parcelas parcela : parcelas) {
                document.add(new Paragraph(
                        "Parcela " + parcela.getNumeroParcela() + ": " +
                                "Valor Parcela = R$ " + String.format("%.2f", parcela.getValorParcela()) + ", " +
                                "Valor Amortização = R$ " + String.format("%.2f", parcela.getValorAmortizacao())
                ));
            }

            System.out.println("PDF gerado com sucesso: SimulacaoFinanciamento_" + idFinanciamento + ".pdf");
        } catch (DocumentException | IOException e) {
            System.err.println("Erro ao gerar o PDF: " + e.getMessage());
        } finally {
            document.close();
        }
    }


}
