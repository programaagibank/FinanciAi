# FinanciAí

## Descrição
O **FinanciAí** é uma solução inovadora no quesito de simulação rápida, prática e eficiente para financiamentos imobiliários. Desenvolvido pensando em ser um simulador geral, o programa oferece liberdade total ao usuário para digitar todos os valores, incluindo taxa e valor de entrada. O simulador realiza os cálculos para dois tipos de amortização: **PRICE** e **SAC**, permitindo comparar o valor das parcelas, o total pago e a evolução da dívida ao longo do tempo.

## Ferramentas e Linguagens Utilizadas
- **Jira**: Para gerenciamento de tarefas e acompanhamento do projeto.
- **DBeaver**: Para interação com o banco de dados MySQL.
- **IntelliJ**: IDE utilizada para o desenvolvimento.
- **Java**: Linguagem de programação utilizada no desenvolvimento.
- **MySQL**: Banco de dados para persistência de dados.

---

## Como Funciona

### Cálculos Realizados

### 1. **Simulação PRICE**
No sistema **PRICE**, as parcelas são fixas, ou seja, o valor da parcela é o mesmo durante todo o financiamento, mas a composição das parcelas (amortização e juros) varia ao longo do tempo.

#### Fórmula do cálculo da parcela fixa:
A fórmula para calcular a **parcela fixa** (que é constante durante todo o financiamento) é dada por:

```math
\text{Parcela Fixa} = \text{Valor do Financiamento} \times \frac{\text{Taxa de Juros}}{1 - (1 + \text{Taxa de Juros})^{-\text{Prazo}}}
```

Onde:
- **Valor do Financiamento** é o valor total que você deseja financiar.
- **Taxa de Juros** é a taxa de juros mensal aplicada ao financiamento.
- **Prazo** é o número total de meses do financiamento.

#### Cálculo da Amortização:
A **amortização** em cada parcela do financiamento é calculada da seguinte forma:

```math
\text{Amortização} = \text{Parcela Fixa} - (\text{Saldo Devedor} \times \text{Taxa de Juros})
```

Onde:
- **Saldo Devedor** é o valor restante do financiamento a ser pago, que diminui a cada pagamento de parcela.

#### Total Pago:
O **total pago** ao longo do financiamento é dado pela soma de todas as parcelas fixas:

```math
\text{Total Pago} = \text{Parcela Fixa} \times \text{Prazo}
```

---

### 2. **Simulação SAC (Sistema de Amortização Constante)**
No **SAC**, o valor da amortização é constante durante todo o financiamento, o que resulta em parcelas que diminuem ao longo do tempo.

#### Fórmula do cálculo da parcela:
A fórmula para calcular a **parcela** em cada mês é:

```math
\text{Parcela} = \text{Amortização} + (\text{Saldo Devedor} \times \text{Taxa de Juros})
```

Onde:
- **Amortização** é o valor fixo que será abatido do saldo devedor a cada mês e é calculado por:

```math
\text{Amortização} = \frac{\text{Valor do Financiamento}}{\text{Prazo}}
```

- **Saldo Devedor** é o valor restante do financiamento após a amortização de cada mês.

#### Total Pago:
O **total pago** ao longo do financiamento é dado pela soma de todas as parcelas:

```math
\text{Total Pago} = \sum_{i=1}^{\text{Prazo}} \text{Parcela}_i
```

Onde:
- **Parcela** varia a cada mês, diminuindo à medida que o saldo devedor é reduzido pela amortização.

---

## Exemplo de Uso

### PRICE

Para calcular o financiamento utilizando o método PRICE, basta inserir os valores do financiamento, taxa de juros e prazo. O sistema calculará as parcelas fixas, a amortização e o total pago.

#### Exemplo:
- **Valor do Financiamento**: R$ 100.000,00
- **Taxa de Juros**: 0,5% ao mês (0,005)
- **Prazo**: 240 meses (20 anos)

A **parcela fixa** será calculada usando a fórmula do PRICE:

```math
\text{Parcela Fixa} = 100000 \times \frac{0.005}{1 - (1 + 0.005)^{-240}} \approx 808.25
```

O **total pago** ao longo do financiamento será:

```math
\text{Total Pago} = 808.25 \times 240 = 194000.00
```

### SAC

Para calcular o financiamento utilizando o método SAC, o valor da **amortização** é constante, e as parcelas vão diminuindo com o tempo.

#### Exemplo:
- **Valor do Financiamento**: R$ 100.000,00
- **Taxa de Juros**: 0,5% ao mês (0,005)
- **Prazo**: 240 meses (20 anos)

A **amortização** será calculada por:

```math
\text{Amortização} = \frac{100000}{240} = 416.67
```

A **primeira parcela** será:

```math
\text{Parcela 1} = 416.67 + (100000 \times 0.005) = 416.67 + 500 = 916.67
```


E assim por diante, com as parcelas diminuindo à medida que o saldo devedor é abatido.


