package financiai.financiai.util;

import financiai.financiai.model.Cliente;
import financiai.financiai.model.Financiamento;
import financiai.financiai.model.Imovel;
import financiai.financiai.model.Parcela;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContratoFinanciamentoPDF {
    public static void gerarContratoPDF(Financiamento financiamento, Cliente cliente,
                                        Imovel imovel, String nomeBanco) throws Exception {
        Document documento = new Document();
        try {
            String nomeArquivo = "Contrato_Financiamento_" + cliente.getCpf() + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(nomeArquivo));
            documento.open();

            Font fonteTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fonteClausula = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fonteTexto = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font fontePequena = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font fonteCabecalhoTabela = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

            // Cabeçalho
            Paragraph titulo = new Paragraph("CONTRATO DE FINANCIAMENTO", fonteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);
            documento.add(Chunk.NEWLINE);

            // Preâmbulo - usando nome do banco dinâmico
            Paragraph preambulo = new Paragraph(
                    "Por este instrumento particular, de um lado " + nomeBanco + ", com sede na cidade de São Paulo-SP, " +
                            "Rua das Finanças, 123, inscrito no CNPJ sob o nº 00.000.000/0001-00, de ora em diante denominada " +
                            "CREDORA e, de outro lado, " + cliente.getNome() + ", " +
                            "portador(a) do CPF nº " + formatarCPF(cliente.getCpf()) + ", residente em [ENDEREÇO DO CLIENTE], " +
                            "de ora em diante denominado(a) CONTRATANTE, têm, entre si, como justo e contratado, " +
                            "o que se segue:", fonteTexto);
            documento.add(preambulo);
            documento.add(Chunk.NEWLINE);

            // CLÁUSULA 1ª - Descrição do bem
            adicionarClausula(documento, "1ª",
                    "O CONTRATANTE " + cliente.getNome() + " adquiriu da vendedora " + nomeBanco + ", com sede na cidade de [CIDADE VENDEDOR], " +
                            "[ENDEREÇO VENDEDOR], inscrita no CNPJ sob o nº [CNPJ VENDEDOR], o seguinte bem: " +
                            imovel.getTipoImovel() + ", pelo valor de R$ " + String.format("%.2f", imovel.getValorImovel()) +
                            " (REAIS).", fonteClausula, fonteTexto);

            // CLÁUSULA 2ª - Valor financiado
            adicionarClausula(documento, "2ª",
                    "A CREDORA " + nomeBanco + " entregará ao CONTRATANTE " + cliente.getNome() + ", através de carta de crédito, o valor de R$ " +
                            String.format("%.2f", financiamento.getValorFinanciado()) + " (REAIS), para utilizá-lo como " +
                            "pagamento de parte do preço do bem identificado na cláusula anterior, valor esse que passará " +
                            "o CONTRATANTE " + cliente.getNome() + " a dever, como principal, à CREDORA " + nomeBanco + ".", fonteClausula, fonteTexto);

            // CLÁUSULA 3ª - Obrigações do contratante
            adicionarClausula(documento, "3ª",
                    "O CONTRATANTE " + cliente.getNome() + ", por este ato, confessa-se devedor da CREDORA " + nomeBanco + " pela quantia que dela neste ato " +
                            "recebeu, estipulada na cláusula anterior, obrigando-se a pagá-la acrescida de: juros, comissões, " +
                            "correção monetária; do valor do imposto sobre operações financeiras incidente sobre este contrato; " +
                            "das taxas de aceite e distribuição de letras de câmbio que constam deste mesmo instrumento.",
                    fonteClausula, fonteTexto);

            // CLÁUSULA 4ª - Condições de pagamento
            adicionarClausula(documento, "4ª",
                    "O valor globalizando o principal e encargos, fixados na cláusula anterior, será pago pelo " +
                            "CONTRATANTE " + cliente.getNome() + " em " + financiamento.getPrazo() + " prestações mensais, iguais e sucessivas, vencendo-se " +
                            "a primeira no dia [DIA VENCIMENTO], e todas as demais em igual dia, em cada mês sucessivo após o " +
                            "vencimento da primeira, totalizando a dívida em R$ " + String.format("%.2f", financiamento.getTotalPagar()) +
                            " (REAIS).", fonteClausula, fonteTexto);

            // TABELA DE PARCELAS
            documento.add(new Paragraph("\nTabela de Parcelas:", fonteClausula));

            PdfPTable tabelaParcelas = new PdfPTable(5);
            tabelaParcelas.setWidthPercentage(100);
            tabelaParcelas.setSpacingBefore(10f);
            tabelaParcelas.setSpacingAfter(10f);

            adicionarCelulaCabecalho(tabelaParcelas, "Parcela", fonteCabecalhoTabela);
            adicionarCelulaCabecalho(tabelaParcelas, "Valor (R$)", fonteCabecalhoTabela);
            adicionarCelulaCabecalho(tabelaParcelas, "Amortização (R$)", fonteCabecalhoTabela);
            adicionarCelulaCabecalho(tabelaParcelas, "Juros (R$)", fonteCabecalhoTabela);
            adicionarCelulaCabecalho(tabelaParcelas, "Saldo Devedor (R$)", fonteCabecalhoTabela);

            List<Parcela> parcelas = financiamento.getParcelas();
            for (Parcela parcela : parcelas) {
                tabelaParcelas.addCell(new Phrase(String.valueOf(parcela.getNumeroParcela()), fonteTexto));
                tabelaParcelas.addCell(new Phrase(String.format("%.2f", parcela.getValorParcela()), fonteTexto));
                tabelaParcelas.addCell(new Phrase(String.format("%.2f", parcela.getValorAmortizacao()), fonteTexto));
                tabelaParcelas.addCell(new Phrase(String.format("%.2f", parcela.getValorJuros()), fonteTexto));
                tabelaParcelas.addCell(new Phrase(String.format("%.2f", parcela.getSaldoDevedor()), fonteTexto));
            }

            documento.add(tabelaParcelas);
            documento.add(Chunk.NEWLINE);

            // CLÁUSULA 5ª - Instrumento de pagamento
            adicionarClausula(documento, "5ª",
                    "Como instrumento controlador do pagamento das prestações devidas pelo CONTRATANTE " + cliente.getNome() + ", a CREDORA " + nomeBanco + " " +
                            "entregará ao CONTRATANTE " + cliente.getNome() + " um carnê contendo avisos-recibos, um para cada uma das prestações ajustadas " +
                            "na forma deste contrato, sendo que esse carnê deverá ser apresentado pelo CONTRATANTE " + cliente.getNome() + " no ato de " +
                            "pagamento de cada uma de suas prestações, sendo que a quitação se dará por autenticação mecânica, " +
                            "correspondente a cada uma das prestações que houverem sido pagas.", fonteClausula, fonteTexto);

            // CLÁUSULA 7ª - Inadimplência
            adicionarClausula(documento, "7ª",
                    "Vencida e não paga qualquer das prestações do financiamento, a CREDORA " + nomeBanco + " poderá sacar uma " +
                            "letra de câmbio à vista contra o CONTRATANTE " + cliente.getNome() + " pelo valor da prestação em mora, levando-a a protesto.",
                    fonteClausula, fonteTexto);

            // CLÁUSULA 8ª - Nota promissória
            adicionarClausula(documento, "8ª",
                    "Para utilização exclusivamente em caso de inadimplência, o CONTRATANTE " + cliente.getNome() + " emite a favor da CREDORA " + nomeBanco + " " +
                            "uma Nota Promissória, pelo valor total de sua obrigação, incluindo o principal e encargos, sem " +
                            "vencimento expresso. Se o CONTRATANTE " + cliente.getNome() + " incidir em impontualidade, insolvência ou infração de " +
                            "obrigação legal ou contratual, a CREDORA " + nomeBanco + " anotará na referida Nota Promissória o total das " +
                            "prestações recebidas e a levará a protesto pelo saldo devedor que se considerará antecipadamente " +
                            "vencido e exigível de pleno direito. Caberão à CREDORA " + nomeBanco + " os direitos e ações outorgados pelo " +
                            "Decreto-lei nº 911/69 e legislação posterior aplicável, ficando desde já investida dos " +
                            "necessários poderes para retomar, vender e transferir aos compradores os bens dos quais, por " +
                            "este contrato, se tornou proprietária fiduciária.",
                    fonteClausula, fonteTexto);

            // CLÁUSULA 9ª - Alienação fiduciária
            adicionarClausula(documento, "9ª",
                    "Em garantia das obrigações principais e acessórias ora contratadas, o CONTRATANTE " + cliente.getNome() + " transfere à " +
                            "CREDORA " + nomeBanco + ", em alienação fiduciária, o bem identificado na cláusula 1ª deste instrumento e dos " +
                            "demais elementos identificadores que ficam fazendo parte deste contrato.", fonteClausula, fonteTexto);

            // CLÁUSULA 10ª - Letras de câmbio
            adicionarClausula(documento, "10ª",
                    "Para recomposição de seu Caixa, com os recursos que empregou para a realização deste financiamento, " +
                            "a CREDORA " + nomeBanco + " aceitará, a débito do CONTRATANTE " + cliente.getNome() + ", letras de câmbio ao portador, sacadas pela " +
                            "interveniente [NOME VENDEDOR], colocando essas letras no mercado de capitais, lastreadas pela " +
                            "Nota Promissória referida na cláusula 8ª.", fonteClausula, fonteTexto);

            // CLÁUSULA 11ª - Encargos por mora
            adicionarClausula(documento, "11ª",
                    "Na ocorrência de mora do CONTRATANTE " + cliente.getNome() + ", serão cobrados do mesmo, na Data da efetiva liquidação de " +
                            "seus débitos, encargos à taxa máxima que estiver sendo praticada pela CREDORA " + nomeBanco + " e que, em " +
                            "hipótese alguma, será inferior às taxas estipuladas neste contrato e, se feita a cobrança, o " +
                            "CONTRATANTE " + cliente.getNome() + " ficará, ainda, sujeito ao pagamento das custas, demais despesas e honorários de " +
                            "advogado nunca inferiores a 10% sobre o valor da condenação.", fonteClausula, fonteTexto);

            // CLÁUSULA 12ª - Rescisão antecipada
            adicionarClausula(documento, "12ª",
                    "Excluso o caso de rescisão antecipada por inadimplência, a liquidação deste contrato, antes de " +
                            "seu vencimento, fica condicionada à expressa anuência da CREDORA " + nomeBanco + ".", fonteClausula, fonteTexto);

            // CLÁUSULA 13ª - Foro
            adicionarClausula(documento, "13ª",
                    "As partes elegem como seu domicílio imutável, para a propositura de qualquer ação resultante " +
                            "deste contrato, a Comarca de [COMARCA]. Por estarem, assim, ajustadas, as partes assinam este " +
                            "contrato na presença das testemunhas abaixo.", fonteClausula, fonteTexto);

            // Rodapé e assinaturas
            documento.add(Chunk.NEWLINE);
            Paragraph localData = new Paragraph(
                    "São Paulo, " + new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(new Date()) + ".",
                    fonteTexto);
            localData.setAlignment(Element.ALIGN_CENTER);
            documento.add(localData);
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);

            // Assinatura do Contratante
            Paragraph assinaturaCliente = new Paragraph("___________________________", fonteTexto);
            assinaturaCliente.add(new Paragraph(cliente.getNome() + " - CONTRATANTE", fonteTexto));
            assinaturaCliente.add(new Paragraph("CPF: " + formatarCPF(cliente.getCpf()), fontePequena));
            assinaturaCliente.setAlignment(Element.ALIGN_CENTER);
            documento.add(assinaturaCliente);
            documento.add(Chunk.NEWLINE);

            // Assinatura do Credor
            Paragraph assinaturaBanco = new Paragraph("___________________________", fonteTexto);
            assinaturaBanco.add(new Paragraph(nomeBanco + " - CREDORA", fonteTexto));
            assinaturaBanco.add(new Paragraph("CNPJ: 00.000.000/0001-00", fontePequena));
            assinaturaBanco.setAlignment(Element.ALIGN_CENTER);
            documento.add(assinaturaBanco);
            documento.add(Chunk.NEWLINE);

            documento.close();
        } catch (Exception e) {
            throw new Exception("Erro ao gerar contrato PDF: " + e.getMessage());
        }
    }

    private static void adicionarCelulaCabecalho(PdfPTable tabela, String texto, Font fonte) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fonte));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(220, 220, 220));
        tabela.addCell(cell);
    }

    private static void adicionarClausula(Document documento, String numero, String texto,
                                          Font fonteClausula, Font fonteTexto) throws DocumentException {
        documento.add(new Paragraph("CLÁUSULA " + numero + " -", fonteClausula));
        documento.add(new Paragraph(texto, fonteTexto));
        documento.add(Chunk.NEWLINE);
    }

    private static String formatarCPF(String cpf) {
        if(cpf == null || cpf.length() != 11) {
            return "[CPF NÃO INFORMADO]";
        }
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}