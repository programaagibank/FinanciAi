package org.financiai.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.financiai.model.entities.Financiamento;
import org.financiai.model.entities.Parcelas;
import org.financiai.model.entities.Cliente;
import org.financiai.model.entities.Imovel;
import org.financiai.model.enums.TipoImovel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeradorPDF {

    public static void gerarPDF(Financiamento financiamento, Cliente cliente, Imovel imovel, List<Parcelas> parcelas) {
        Document document = new Document();

        try {
            // Cria o arquivo PDF
            PdfWriter.getInstance(document, new FileOutputStream("SimulacaoFinanciamento_" + financiamento.getFinanciamentoId() + ".pdf"));
            document.open();

            // Adiciona conteúdo ao PDF
            document.add(new Paragraph("Resultado da Simulação"));
            document.add(new Paragraph("ID do Financiamento: " + financiamento.getFinanciamentoId()));
            document.add(new Paragraph("Cliente: " + cliente.getNome())); // Usando o cliente passado como parâmetro
            document.add(new Paragraph("Tipo de Imóvel: " + imovel.getTipoImovel())); // Usando o imóvel passado como parâmetro
            document.add(new Paragraph("Valor do Imóvel: R$ " + String.format("%.2f", imovel.getValorImovel()))); // Usando o imóvel passado como parâmetro
            document.add(new Paragraph("Valor de Entrada: R$ " + String.format("%.2f", financiamento.getValorEntrada())));
            document.add(new Paragraph("Valor Financiado: R$ " + String.format("%.2f", financiamento.getValorFinanciado())));
            document.add(new Paragraph("\nSistema Price:"));
            document.add(new Paragraph("Total pago: R$ " + String.format("%.2f", financiamento.getTotalPagar())));
            document.add(new Paragraph("Juros totais: R$ " + String.format("%.2f", financiamento.getTotalPagar() - financiamento.getValorFinanciado())));
            document.add(new Paragraph("\nSistema SAC:"));
            document.add(new Paragraph("Total pago: R$ " + String.format("%.2f", financiamento.getTotalPagar())));
            document.add(new Paragraph("Juros totais: R$ " + String.format("%.2f", financiamento.getTotalPagar() - financiamento.getValorFinanciado())));
            document.add(new Paragraph("\nTabela de Parcelas:"));

            // Adiciona as parcelas ao PDF
            for (Parcelas parcela : parcelas) {
                document.add(new Paragraph(
                        "Parcela " + parcela.getNumeroParcela() + ": " +
                                "Valor Parcela = R$ " + String.format("%.2f", parcela.getValorParcela()) + ", " +
                                "Valor Amortização = R$ " + String.format("%.2f", parcela.getValorAmortizacao())
                ));
            }

            System.out.println("PDF gerado com sucesso: SimulacaoFinanciamento_" + financiamento.getFinanciamentoId() + ".pdf");
        } catch (DocumentException | IOException e) {
            System.err.println("Erro ao gerar o PDF: " + e.getMessage());
        } finally {
            document.close();
        }
    }
}