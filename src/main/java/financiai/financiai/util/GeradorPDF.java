package financiai.financiai.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import financiai.financiai.model.Cliente;
import financiai.financiai.model.Financiamento;
import financiai.financiai.model.Imovel;
import financiai.financiai.model.Parcela;

import javax.imageio.ImageIO;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GeradorPDF {

    public static void gerarPDF(Financiamento financiamento, Cliente cliente, Imovel imovel, List<Parcela> parcelas) throws IOException, DocumentException {
        Document document = new Document();

        try {
            // Cria o arquivo PDF
            String fileName = "SimulacaoFinanciamento_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open(); // 🔹 Abre o documento ANTES de adicionar qualquer conteúdo

            // 🔹 Adiciona uma página com um título para evitar erro
            document.add(new Paragraph(" "));

            // Configuração de fontes
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);

            // 🔹 Adiciona a LOGO no começo do PDF
            InputStream inputStream = GeradorPDF.class.getClassLoader().getResourceAsStream("gg.png");
            if (inputStream != null) {
                Image logo = Image.getInstance(ImageIO.read(inputStream), null);
                logo.scaleToFit(150, 150);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
            } else {
                document.add(new Paragraph("⚠ Logo não encontrada!", headerFont)); // Mensagem caso a logo falhe
            }

            // 🔹 Adiciona título principal
            Paragraph title = new Paragraph("Simulação de Financiamento", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // 🔹 Cria uma tabela com 2 colunas para cabeçalho
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setSpacingBefore(10);
            headerTable.setSpacingAfter(10);

            // Célula vazia para alinhamento
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(emptyCell);

            // Adiciona informações do financiamento
            PdfPCell infoCell = new PdfPCell();
            infoCell.setBorder(Rectangle.NO_BORDER);

            Paragraph infoParagraph = new Paragraph();
            infoParagraph.add(new Paragraph("Cliente: " + cliente.getNome(), normalFont));
            infoParagraph.add(new Paragraph("CPF: " + cliente.getCpf(), normalFont));
            infoParagraph.add(new Paragraph("Tipo de Imóvel: " + imovel.getTipoImovel(), normalFont));
            infoParagraph.add(new Paragraph("Valor do Imóvel: R$ " + String.format("%.2f", imovel.getValorImovel()), normalFont));
            infoParagraph.add(new Paragraph("Valor de Entrada: R$ " + String.format("%.2f", financiamento.getValorEntrada()), normalFont));
            infoParagraph.add(new Paragraph("Valor Financiado: R$ " + String.format("%.2f", financiamento.getValorFinanciado()), normalFont));
            infoParagraph.add(new Paragraph("Taxa de Juros: " + String.format("%.2f", financiamento.getTaxaJuros() * 100) + "%", normalFont));
            infoParagraph.add(new Paragraph("Prazo: " + financiamento.getPrazo() + " meses", normalFont));
            infoParagraph.add(new Paragraph("Tipo de Amortização: " + financiamento.getTipoAmortizacao(), normalFont));
            infoParagraph.add(new Paragraph("Total a Pagar: R$ " + String.format("%.2f", financiamento.getTotalPagar()), normalFont));

            infoCell.addElement(infoParagraph);
            headerTable.addCell(infoCell);
            document.add(headerTable);

            // 🔹 Adiciona tabela de parcelas
            document.add(new Paragraph("\nTabela de Parcelas:", headerFont));
            PdfPTable table = new PdfPTable(4); // 4 colunas
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            // Cabeçalho da tabela
            table.addCell(new PdfPCell(new Phrase("Parcela", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Valor (R$)", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Amortização (R$)", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Juros (R$)", headerFont)));

            // Adiciona as parcelas à tabela
            for (Parcela parcela : parcelas) {
                table.addCell(new Phrase(String.valueOf(parcela.getNumeroParcela()), normalFont));
                table.addCell(new Phrase(String.format("%.2f", parcela.getValorParcela()), normalFont));
                table.addCell(new Phrase(String.format("%.2f", parcela.getValorAmortizacao()), normalFont));
                table.addCell(new Phrase(String.format("%.2f", parcela.getValorJuros()), normalFont));
            }

            document.add(table);

            // 🔹 Adiciona data de emissão
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Paragraph footer = new Paragraph("\nData de Emissão: " + dateFormat.format(new Date()), normalFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

        } catch (DocumentException | IOException e) {
            throw e; // Re-lança a exceção para ser tratada pelo chamador
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }
}

