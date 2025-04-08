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
                                        Imovel imovel) throws Exception {
        Document documento = new Document();
        try {
            // Parameter validation
            if (cliente == null || financiamento == null || imovel == null) {
                throw new IllegalArgumentException("Parameters cannot be null");
            }

            LocalDate hoje = LocalDate.now();
            String numeroProposta = "PROP-" + hoje.format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                    "-" + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
            String nomeArquivo = "Carta_Proposta_" + numeroProposta + "_" + formatarCPF(cliente.getCpf()) + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(nomeArquivo));
            documento.open();


            Font fonteTituloPrincipal = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fonteCabecalho = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fonteTexto = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font fonteNegrito = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontePequena = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font fonteClausula = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fonteCabecalhoTabela = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor corFundoCabecalho = new BaseColor(0, 102, 204); // Dark blue


            try {

                Image imagemLogo;
                String imagePath = "/images/Luxury Modern Real Estate.png";


                URL imgUrl = ContratoFinanciamentoPDF.class.getResource(imagePath);
                if (imgUrl != null) {
                    imagemLogo = Image.getInstance(imgUrl);
                } else {

                    imagemLogo = Image.getInstance("src/main/resources" + imagePath);

                }


                imagemLogo.scaleToFit(80, 80); // Image size
                imagemLogo.setAlignment(Image.ALIGN_CENTER);


                PdfPTable tabelaCabecalho = new PdfPTable(2);
                tabelaCabecalho.setWidthPercentage(100);
                tabelaCabecalho.setSpacingAfter(10f);


                float[] columnWidths = {30f, 70f};
                tabelaCabecalho.setWidths(columnWidths);


                PdfPCell cellImagem = new PdfPCell(imagemLogo, true);
                cellImagem.setBorder(Rectangle.NO_BORDER);
                cellImagem.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellImagem.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellImagem.setPadding(5);


                PdfPCell cellTexto = new PdfPCell();
                cellTexto.setBorder(Rectangle.NO_BORDER);
                cellTexto.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellTexto.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellTexto.setPadding(5);


                Paragraph enderecoBanco = new Paragraph("Rua das Finanças, 123, São Paulo - SP", fontePequena);
                enderecoBanco.setAlignment(Element.ALIGN_CENTER);
                cellTexto.addElement(enderecoBanco);

                Paragraph cnpjBanco = new Paragraph("CNPJ: 00.000.000/0001-00", fontePequena);
                cnpjBanco.setAlignment(Element.ALIGN_CENTER);
                cellTexto.addElement(cnpjBanco);

                Paragraph telefoneBanco = new Paragraph("Telefone: (11) 1234-5678", fontePequena);
                telefoneBanco.setAlignment(Element.ALIGN_CENTER);
                cellTexto.addElement(telefoneBanco);


                tabelaCabecalho.addCell(cellImagem);
                tabelaCabecalho.addCell(cellTexto);


                documento.add(tabelaCabecalho);

            } catch (Exception e) {
                System.err.println("ERROR: Could not load image. Reason: " + e.getMessage());


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


            documento.add(Chunk.NEWLINE);


            Paragraph titulo = new Paragraph("CARTA PROPOSTA DE FINANCIAMENTO", fonteTituloPrincipal);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            documento.add(Chunk.NEWLINE);


            Paragraph para = new Paragraph("PARA:", fonteNegrito);
            documento.add(para);

            Paragraph nomeCliente = new Paragraph(cliente.getNome() != null ? cliente.getNome() : "[NOME NÃO INFORMADO]", fonteTexto);
            documento.add(nomeCliente);

            Paragraph cpfCliente = new Paragraph("CPF: " + formatarCPF(cliente.getCpf()), fonteTexto);
            documento.add(cpfCliente);

            documento.add(Chunk.NEWLINE);


            Paragraph saudacao = new Paragraph("Prezado(a) " +
                    (cliente.getNome() != null ? cliente.getNome() : "Cliente") + ",", fonteTexto);
            documento.add(saudacao);

            documento.add(Chunk.NEWLINE);


            Paragraph introducao = new Paragraph(
                    "Temos o prazer de apresentar nossa proposta de financiamento para a aquisição do " +
                            (imovel.getTipoImovel() != null ? imovel.getTipoImovel() : "[TIPO DE IMÓVEL]") +
                            " desejado, visando proporcionar as melhores " +
                            "condições para viabilizar seu investimento com segurança e transparência.",
                    fonteTexto);
            documento.add(introducao);

            documento.add(Chunk.NEWLINE);


            adicionarItemNumerado(documento, "1", "Objeto da Proposta",
                    "A FinanciAí" + " oferece a você, " + cliente.getNome() + ", um financiamento no valor de R$ " +
                            String.format("%.2f", financiamento.getValorFinanciado()) + " (" + converterNumero(financiamento.getValorFinanciado()) + "), para ser " +
                            "utilizado como parte do pagamento do imóvel adquirido. O valor total do imóvel é de R$ " +
                            String.format("%.2f", imovel.getValorImovel()) + " (" + converterNumero(imovel.getValorImovel()) + "), e o " +
                            "financiamento concedido ajudará a viabilizar sua aquisição de maneira facilitada.",
                    fonteNegrito, fonteTexto);


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


            documento.add(new Paragraph("\nTabela de Parcelas:", fonteClausula));

            PdfPTable tabelaParcelas = new PdfPTable(5);
            tabelaParcelas.setWidthPercentage(100);
            tabelaParcelas.setSpacingBefore(10f);
            tabelaParcelas.setSpacingAfter(10f);


            adicionarCelulaCabecalho(tabelaParcelas, "Parcela", fonteCabecalhoTabela, corFundoCabecalho);
            adicionarCelulaCabecalho(tabelaParcelas, "Valor (R$)", fonteCabecalhoTabela, corFundoCabecalho);
            adicionarCelulaCabecalho(tabelaParcelas, "Amortização (R$)", fonteCabecalhoTabela, corFundoCabecalho);
            adicionarCelulaCabecalho(tabelaParcelas, "Juros (R$)", fonteCabecalhoTabela, corFundoCabecalho);
            adicionarCelulaCabecalho(tabelaParcelas, "Saldo Devedor (R$)", fonteCabecalhoTabela, corFundoCabecalho);


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


            adicionarItemNumerado(documento, "3", "Garantias e Pagamento",
                    "Para garantir a segurança do financiamento, o imóvel adquirido será dado em alienação fiduciária à FinanciAí " +
                            " até a quitação integral do financiamento. Um carnê será fornecido para facilitar os pagamentos.",
                    fonteNegrito, fonteTexto);


            adicionarItemNumerado(documento, "4", "Condições de Inadimplência",
                    "Caso haja atraso no pagamento das parcelas, a FinanciAí poderá tomar as medidas cabíveis, " +
                            "incluindo cobrança de encargos moratórios e protesto da Nota Promissória correspondente ao saldo devedor.",
                    fonteNegrito, fonteTexto);


            adicionarItemNumerado(documento, "5", "Liquidação Antecipada",
                    "Se desejar liquidar o financiamento antes do prazo estabelecido, será necessária a anuência expressa da FinanciAí.",
                    fonteNegrito, fonteTexto);


            adicionarItemNumerado(documento, "6", "Foro de Eleição",
                    "Para dirimir quaisquer dúvidas, fica eleito o foro da comarca de São Paulo/SP.",
                    fonteNegrito, fonteTexto);

            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);


            Paragraph assinatura = new Paragraph("Atenciosamente,", fonteTexto);
            assinatura.setAlignment(Element.ALIGN_CENTER);
            documento.add(assinatura);

            documento.add(Chunk.NEWLINE);

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


        Chunk numChunk = new Chunk(numero + ". ", fonteNegrito);
        Chunk tituloChunk = new Chunk(titulo, fonteNegrito);
        item.add(numChunk);
        item.add(tituloChunk);
        documento.add(item);


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


            if (inteiro == 0 && centavos == 0) return "zero reais";
            if (inteiro == 1 && centavos == 0) return "um real";
            if (inteiro == 0 && centavos == 1) return "um centavo";
            if (inteiro == 0 && centavos > 1) return converterNumero(centavos, unidades, dez_a_dezenove, dezenas) + " centavos";

            StringBuilder extenso = new StringBuilder();


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


            if (inteiro > 0) {
                extenso.append(converterNumero(inteiro, unidades, dez_a_dezenove, dezenas));
            }


            if (extenso.length() > 0) {
                extenso.append(extenso.toString().contains("mil") && !extenso.toString().contains("e") ? " reais" : " reais");
            }


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


        if (valor >= 100) {
            extenso.append(centenas[valor / 100]);
            valor %= 100;
            if (valor > 0) {
                extenso.append(" e ");
            }
        }


        if (valor > 0) {
            extenso.append(converterNumero(valor, unidades, dez_a_dezenove, dezenas));
        }

        return extenso.toString();
    }
}