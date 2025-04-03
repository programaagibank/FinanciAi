package financiai.financiai.util;

import java.time.LocalDate;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import financiai.financiai.model.Cliente;
import financiai.financiai.model.Financiamento;
import financiai.financiai.model.Imovel;
import financiai.financiai.model.Parcela;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.io.FileOutputStream;
import java.util.List;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class ContratoFinanciamentoPDF {

    // Helper methods for table cells
    private static void adicionarCelulaCabecalho(PdfPTable tabela, String texto, Font fonte, BaseColor corFundo) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fonte));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(corFundo);
        cell.setPadding(5);
        cell.setBorderWidth(1);
        cell.setBorderColor(BaseColor.WHITE);
        tabela.addCell(cell);
    }

    private static void adicionarCelulaConteudo(PdfPTable tabela, String texto, Font fonte) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fonte));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        cell.setBorderWidth(1);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        tabela.addCell(cell);
    }

    public static void gerarContratoPDF(Financiamento financiamento, Cliente cliente,
                                        Imovel imovel, String nomeBanco) throws Exception {
        Document documento = new Document();
        try {
            // Parameter validation
            if (cliente == null || financiamento == null || imovel == null || nomeBanco == null) {
                throw new IllegalArgumentException("Parameters cannot be null");
            }

            LocalDate hoje = LocalDate.now();
            String numeroProposta = "PROP-" + hoje.format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                    "-" + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
            String nomeArquivo = "Carta_Proposta_" + numeroProposta + "_" + formatarCPF(cliente.getCpf()) + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(nomeArquivo));
            documento.open();

            // Font definitions
            Font fonteTituloPrincipal = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fonteCabecalho = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fonteTexto = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font fonteNegrito = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontePequena = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font fonteClausula = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fonteCabecalhoTabela = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor corFundoCabecalho = new BaseColor(0, 102, 204); // Dark blue

            // ==============================================
            // HEADER WITH IMAGE - WORKING VERSION
            // ==============================================
            try {
                // 1. Load image robustly
                Image imagemLogo;
                String imagePath = "/images/Luxury Modern Real Estate.png"; // Path relative to resources folder

                // Try loading from classpath (recommended for Java projects)
                URL imgUrl = ContratoFinanciamentoPDF.class.getResource(imagePath);
                if (imgUrl != null) {
                    imagemLogo = Image.getInstance(imgUrl);
                } else {
                    // If not found in classpath, try filesystem path
                    imagemLogo = Image.getInstance("src/main/resources" + imagePath);

                }

                // 2. Configure image
                imagemLogo.scaleToFit(80, 80); // Image size
                imagemLogo.setAlignment(Image.ALIGN_CENTER);

                // 3. Create 2-column table
                PdfPTable tabelaCabecalho = new PdfPTable(2);
                tabelaCabecalho.setWidthPercentage(100);
                tabelaCabecalho.setSpacingAfter(10f);

                // Set column proportions (30% image, 70% text)
                float[] columnWidths = {30f, 70f};
                tabelaCabecalho.setWidths(columnWidths);

                // 4. Image cell
                PdfPCell cellImagem = new PdfPCell(imagemLogo, true);
                cellImagem.setBorder(Rectangle.NO_BORDER);
                cellImagem.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellImagem.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellImagem.setPadding(5);

                // 5. Text cell
                PdfPCell cellTexto = new PdfPCell();
                cellTexto.setBorder(Rectangle.NO_BORDER);
                cellTexto.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellTexto.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTexto.setPadding(5);

                // Add text content
                Paragraph cabecalhoBanco = new Paragraph(nomeBanco.toUpperCase(), fonteCabecalho);
                cabecalhoBanco.setAlignment(Element.ALIGN_CENTER);
                cellTexto.addElement(cabecalhoBanco);

                Paragraph enderecoBanco = new Paragraph("Rua das Finanças, 123, São Paulo - SP", fontePequena);
                enderecoBanco.setAlignment(Element.ALIGN_CENTER);
                cellTexto.addElement(enderecoBanco);

                Paragraph cnpjBanco = new Paragraph("CNPJ: 00.000.000/0001-00", fontePequena);
                cnpjBanco.setAlignment(Element.ALIGN_CENTER);
                cellTexto.addElement(cnpjBanco);

                Paragraph telefoneBanco = new Paragraph("Telefone: (11) 1234-5678", fontePequena);
                telefoneBanco.setAlignment(Element.ALIGN_CENTER);
                cellTexto.addElement(telefoneBanco);

                // Add cells to table
                tabelaCabecalho.addCell(cellImagem);
                tabelaCabecalho.addCell(cellTexto);

                // Add table to document
                documento.add(tabelaCabecalho);

            } catch (Exception e) {
                System.err.println("ERROR: Could not load image. Reason: " + e.getMessage());

                // Fallback - header without image
                Paragraph cabecalhoBanco = new Paragraph(nomeBanco.toUpperCase(), fonteCabecalho);
                cabecalhoBanco.setAlignment(Element.ALIGN_CENTER);
                documento.add(cabecalhoBanco);

                Paragraph enderecoBanco = new Paragraph("Rua das Finanças, 123, São Paulo - SP", fontePequena);
                enderecoBanco.setAlignment(Element.ALIGN_CENTER);
                documento.add(enderecoBanco);

                Paragraph cnpjBanco = new Paragraph("CNPJ: 00.000.000/0001-00", fontePequena);
                cnpjBanco.setAlignment(Element.ALIGN_CENTER);
                documento.add(cnpjBanco);

                Paragraph telefoneBanco = new Paragraph("Telefone: (11) 1234-5678", fontePequena);
                telefoneBanco.setAlignment(Element.ALIGN_CENTER);
                documento.add(telefoneBanco);
            }

            // ==============================================
            // REST OF THE DOCUMENT (ORIGINAL CONTENT)
            // ==============================================
            documento.add(Chunk.NEWLINE);

            // Main title
            Paragraph titulo = new Paragraph("CARTA PROPOSTA DE FINANCIAMENTO", fonteTituloPrincipal);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            documento.add(Chunk.NEWLINE);

            // Recipient
            Paragraph para = new Paragraph("PARA:", fonteNegrito);
            documento.add(para);

            Paragraph nomeCliente = new Paragraph(cliente.getNome() != null ? cliente.getNome() : "[NOME NÃO INFORMADO]", fonteTexto);
            documento.add(nomeCliente);

            Paragraph cpfCliente = new Paragraph("CPF: " + formatarCPF(cliente.getCpf()), fonteTexto);
            documento.add(cpfCliente);

            documento.add(Chunk.NEWLINE);

            // Greeting
            Paragraph saudacao = new Paragraph("Prezado(a) " +
                    (cliente.getNome() != null ? cliente.getNome() : "Cliente") + ",", fonteTexto);
            documento.add(saudacao);

            documento.add(Chunk.NEWLINE);

            // Introduction
            Paragraph introducao = new Paragraph(
                    "Temos o prazer de apresentar nossa proposta de financiamento para a aquisição do " +
                            (imovel.getTipoImovel() != null ? imovel.getTipoImovel() : "[TIPO DE IMÓVEL]") +
                            " desejado, visando proporcionar as melhores " +
                            "condições para viabilizar seu investimento com segurança e transparência.",
                    fonteTexto);
            documento.add(introducao);

            documento.add(Chunk.NEWLINE);

            // 1. Proposal Object
            adicionarItemNumerado(documento, "1", "Objeto da Proposta",
                    "A " + nomeBanco + " oferece a você, " + cliente.getNome() + ", um financiamento no valor de R$ " +
                            String.format("%.2f", financiamento.getValorFinanciado()) + " (" + converterNumero(financiamento.getValorFinanciado()) + "), para ser " +
                            "utilizado como parte do pagamento do imóvel adquirido. O valor total do imóvel é de R$ " +
                            String.format("%.2f", imovel.getValorImovel()) + " (" + converterNumero(imovel.getValorImovel()) + "), e o " +
                            "financiamento concedido ajudará a viabilizar sua aquisição de maneira facilitada.",
                    fonteNegrito, fonteTexto);

            // 2. Financing Conditions
            LocalDate dataAtual = LocalDate.now();
            LocalDate dataVencimento = LocalDate.of(dataAtual.plusMonths(1).getYear(), dataAtual.plusMonths(1).getMonthValue(), 5);

            String nomeMes = dataVencimento.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));

            adicionarItemNumerado(documento, "2", "Condições do Financiamento",
                    "O valor financiado de R$ " + String.format("%.2f", financiamento.getValorFinanciado()) + " será pago em " +
                            financiamento.getPrazo() + " parcelas mensais e sucessivas, com o primeiro vencimento no dia " +
                            dataVencimento.getDayOfMonth() + " de " + nomeMes + " de " + dataVencimento.getYear() + ", " +
                            "e as demais vencendo no mesmo dia dos meses subsequentes. O valor total do financiamento, " +
                            "incluindo encargos e taxas, será de R$ " + String.format("%.2f", financiamento.getTotalPagar()) +
                            " (" + converterNumero(financiamento.getTotalPagar()) + ").",
                    fonteNegrito, fonteTexto);

            // INSTALLMENT TABLE
            documento.add(new Paragraph("\nTabela de Parcelas:", fonteClausula));

            PdfPTable tabelaParcelas = new PdfPTable(5);
            tabelaParcelas.setWidthPercentage(100);
            tabelaParcelas.setSpacingBefore(10f);
            tabelaParcelas.setSpacingAfter(10f);

            // Table headers
            adicionarCelulaCabecalho(tabelaParcelas, "Parcela", fonteCabecalhoTabela, corFundoCabecalho);
            adicionarCelulaCabecalho(tabelaParcelas, "Valor (R$)", fonteCabecalhoTabela, corFundoCabecalho);
            adicionarCelulaCabecalho(tabelaParcelas, "Amortização (R$)", fonteCabecalhoTabela, corFundoCabecalho);
            adicionarCelulaCabecalho(tabelaParcelas, "Juros (R$)", fonteCabecalhoTabela, corFundoCabecalho);
            adicionarCelulaCabecalho(tabelaParcelas, "Saldo Devedor (R$)", fonteCabecalhoTabela, corFundoCabecalho);

            // Table body
            List<Parcela> parcelas = financiamento.getParcelas();
            if (parcelas != null && !parcelas.isEmpty()) {
                for (Parcela parcela : parcelas) {
                    adicionarCelulaConteudo(tabelaParcelas, String.valueOf(parcela.getNumeroParcela()), fonteTexto);
                    adicionarCelulaConteudo(tabelaParcelas, String.format("%.2f", parcela.getValorParcela()), fonteTexto);
                    adicionarCelulaConteudo(tabelaParcelas, String.format("%.2f", parcela.getValorAmortizacao()), fonteTexto);
                    adicionarCelulaConteudo(tabelaParcelas, String.format("%.2f", parcela.getValorJuros()), fonteTexto);
                    adicionarCelulaConteudo(tabelaParcelas, String.format("%.2f", parcela.getSaldoDevedor()), fonteTexto);
                }
            } else {
                PdfPCell cell = new PdfPCell(new Phrase("Nenhuma parcela encontrada", fonteTexto));
                cell.setColspan(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaParcelas.addCell(cell);
            }

            documento.add(tabelaParcelas);
            documento.add(Chunk.NEWLINE);

            // 3. Guarantees and Payment
            adicionarItemNumerado(documento, "3", "Garantias e Pagamento",
                    "Para garantir a segurança do financiamento, o imóvel adquirido será dado em alienação fiduciária à " +
                            nomeBanco + " até a quitação integral do financiamento. Um carnê será fornecido para facilitar os pagamentos.",
                    fonteNegrito, fonteTexto);

            // 4. Default Conditions
            adicionarItemNumerado(documento, "4", "Condições de Inadimplência",
                    "Caso haja atraso no pagamento das parcelas, a " + nomeBanco + " poderá tomar as medidas cabíveis, " +
                            "incluindo cobrança de encargos moratórios e protesto da Nota Promissória correspondente ao saldo devedor.",
                    fonteNegrito, fonteTexto);

            // 5. Early Settlement
            adicionarItemNumerado(documento, "5", "Liquidação Antecipada",
                    "Se desejar liquidar o financiamento antes do prazo estabelecido, será necessária a anuência expressa da " + nomeBanco + ".",
                    fonteNegrito, fonteTexto);

            // 6. Jurisdiction
            adicionarItemNumerado(documento, "6", "Foro de Eleição",
                    "Para dirimir quaisquer dúvidas, fica eleito o foro da comarca de São Paulo/SP.",
                    fonteNegrito, fonteTexto);

            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);

            // Signature
            Paragraph assinatura = new Paragraph("Atenciosamente,", fonteTexto);
            assinatura.setAlignment(Element.ALIGN_CENTER);
            documento.add(assinatura);

            documento.add(Chunk.NEWLINE);

            Paragraph nomeBancoAssinatura = new Paragraph(nomeBanco, fonteNegrito);
            nomeBancoAssinatura.setAlignment(Element.ALIGN_CENTER);
            documento.add(nomeBancoAssinatura);

            Paragraph enderecoBancoAssinatura = new Paragraph("Rua das Finanças, 123, São Paulo - SP", fontePequena);
            enderecoBancoAssinatura.setAlignment(Element.ALIGN_CENTER);
            documento.add(enderecoBancoAssinatura);

            Paragraph cnpjBancoAssinatura = new Paragraph("CNPJ: 00.000.000/0001-00", fontePequena);
            cnpjBancoAssinatura.setAlignment(Element.ALIGN_CENTER);
            documento.add(cnpjBancoAssinatura);

            Paragraph telefoneBancoAssinatura = new Paragraph("Telefone: (11) 1234-5678", fontePequena);
            telefoneBancoAssinatura.setAlignment(Element.ALIGN_CENTER);
            documento.add(telefoneBancoAssinatura);

            documento.close();
        } catch (DocumentException e) {
            throw new Exception("Erro ao gerar documento PDF: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Erro ao gerar carta proposta PDF: " + e.getMessage(), e);
        }
    }

    private static void adicionarItemNumerado(Document documento, String numero, String titulo, String texto,
                                              Font fonteNegrito, Font fonteTexto) throws DocumentException {
        Paragraph item = new Paragraph();

        // Number and title in bold
        Chunk numChunk = new Chunk(numero + ". ", fonteNegrito);
        Chunk tituloChunk = new Chunk(titulo, fonteNegrito);
        item.add(numChunk);
        item.add(tituloChunk);
        documento.add(item);

        // Normal text
        Paragraph textoParagrafo = new Paragraph(texto, fonteTexto);
        documento.add(textoParagrafo);

        documento.add(Chunk.NEWLINE);
    }

    private static String formatarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")) {
            return "[CPF INVÁLIDO]";
        }
        try {
            return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } catch (Exception e) {
            return "[CPF INVÁLIDO]";
        }
    }

    private static String converterNumero(double valor) {
        try {
            if (valor < 0) {
                return "[VALOR NEGATIVO]";
            }

            String[] unidades = {"", "um", "dois", "três", "quatro",
                    "cinco", "seis", "sete", "oito", "nove"};
            String[] dez_a_dezenove = {"dez", "onze", "doze", "treze",
                    "quatorze", "quinze", "dezesseis",
                    "dezessete", "dezoito", "dezenove"};
            String[] dezenas = {"", "dez", "vinte", "trinta", "quarenta",
                    "cinquenta", "sessenta", "setenta",
                    "oitenta", "noventa"};
            String[] centenas = {"", "cento", "duzentos", "trezentos",
                    "quatrocentos", "quinhentos", "seiscentos",
                    "setecentos", "oitocentos", "novecentos"};

            int inteiro = (int) Math.floor(valor);
            int centavos = (int) Math.round((valor - inteiro) * 100);

            // Special cases
            if (inteiro == 0 && centavos == 0) return "zero reais";
            if (inteiro == 1 && centavos == 0) return "um real";
            if (inteiro == 0 && centavos == 1) return "um centavo";
            if (inteiro == 0 && centavos > 1) return converterNumero(centavos, unidades, dez_a_dezenove, dezenas) + " centavos";

            StringBuilder extenso = new StringBuilder();

            // Integer part - thousands
            if (inteiro >= 1000) {
                int milhares = inteiro / 1000;
                if (milhares == 1) {
                    extenso.append("mil");
                } else {
                    extenso.append(converterNumero(milhares, unidades, dez_a_dezenove, dezenas, centenas)).append(" mil");
                }
                inteiro %= 1000;
                if (inteiro > 0) {
                    extenso.append(" e ");
                }
            }

            // Integer part - hundreds
            if (inteiro >= 100) {
                if (inteiro == 100) {
                    extenso.append("cem");
                } else {
                    int centena = inteiro / 100;
                    extenso.append(centenas[centena]);
                }
                inteiro %= 100;
                if (inteiro > 0) {
                    extenso.append(" e ");
                }
            }

            // Integer part - tens and units
            if (inteiro > 0) {
                extenso.append(converterNumero(inteiro, unidades, dez_a_dezenove, dezenas));
            }

            // Add "reais"
            if (extenso.length() > 0) {
                extenso.append(extenso.toString().contains("mil") && !extenso.toString().contains("e") ? " reais" : " reais");
            }

            // Decimal part
            if (centavos > 0) {
                if (extenso.length() > 0) {
                    extenso.append(" e ");
                }
                extenso.append(converterNumero(centavos, unidades, dez_a_dezenove, dezenas));
                extenso.append(centavos == 1 ? " centavo" : " centavos");
            }

            return extenso.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[VALOR NÃO CONVERTIDO]";
        }
    }

    private static String converterNumero(int valor, String[] unidades, String[] dez_a_dezenove, String[] dezenas) {
        if (valor == 0) return "";
        if (valor < 10) return unidades[valor];
        if (valor < 20) return dez_a_dezenove[valor - 10];

        String extenso = dezenas[valor / 10];
        if (valor % 10 != 0) {
            extenso += " e " + unidades[valor % 10];
        }
        return extenso;
    }

    private static String converterNumero(int valor, String[] unidades, String[] dez_a_dezenove,
                                          String[] dezenas, String[] centenas) {
        if (valor == 100) return "cem";

        StringBuilder extenso = new StringBuilder();

        // Hundreds
        if (valor >= 100) {
            extenso.append(centenas[valor / 100]);
            valor %= 100;
            if (valor > 0) {
                extenso.append(" e ");
            }
        }

        // Tens and units
        if (valor > 0) {
            extenso.append(converterNumero(valor, unidades, dez_a_dezenove, dezenas));
        }

        return extenso.toString();
    }
}