# 🏠 FinanciAí 

**FinanciAí** é um simulador de financiamento imobiliário prático e versátil, desenvolvido para oferecer ao usuário uma experiência completa na visualização de condições de financiamento nos sistemas Price e SAC. O sistema calcula parcelas, armazena simulações e permite consultas futuras via CPF.

---

## 🔍 Índice
- [📌 Descrição do Projeto](#-descrição-do-projeto)
- [⚙️ Funcionalidades](#️-funcionalidades)
- [🛠️ Tecnologias](#️-tecnologias)
- [📖 Fórmulas Matemáticas](#-fórmulas-matemáticas)
- [📝 Fluxo de Uso](#-fluxo-de-uso)
  - [1. Preenchimento dos Dados](#1-preenchimento-dos-dados)
  - [2. Simulação](#2-simulação)
  - [3. Resultados](#3-resultados)
  - [4. Consulta de Simulações Anteriores](#4-consulta-de-simulações-anteriores)
- [🧮 Exemplo Prático](#-exemplo-prático)
- [📊 Detalhamento dos Cálculos (SAC)](#-detalhamento-dos-cálculos-sac)
- [🚀 Como Executar o Projeto](#-como-executar-o-projeto)

---


## 📌 Descrição do Projeto

Simulador imobiliário que permite comparar e armazenar financiamentos nos sistemas:
- **Price**: Parcelas iguais com juros decrescentes
- **SAC**: Amortização fixa com parcelas decrescentes

_Todos os cálculos são persistidos em banco MySQL para consulta posterior via CPF._

---

## ⚙️ Funcionalidades

✅ **Geração de PDF**
- Gera um PDF com o detahamento das parcelas e um resumo do financiamento.

✅ **Geração de proposta**
- Gera uma proposta com base na simulação feita.

✅ **Simulação em Tempo Real**  
- Cálculo automático de parcelas, juros e saldo devedor   

✅ **Gestão de Histórico**  
- Armazenamento de clientes, imóveis e financiamentos  
- Consulta por CPF com visualização detalhada  

✅ **Validações**  
- Prevenção de dados inválidos  

---

## 🛠️ Tecnologias

| Componente       | Tecnologias                                                                 |
|------------------|-----------------------------------------------------------------------------|
| Backend          | ![Java](https://img.shields.io/badge/Java-17%2B-blue)                       |
| Interface        | ![JavaFX](https://img.shields.io/badge/JavaFX-19-purple)                    |
| Banco de Dados   | ![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)                     |
| Ferramentas      | ![IntelliJ](https://img.shields.io/badge/IntelliJ_IDEA-2023.1-black)        |

---




## 📖 Fórmulas Matemáticas

### Sistema Price (Parcelas Constantes)
- **Valor da Parcela (PMT):**  
  ```math
  PMT = PV \times \frac{i}{1 - (1 + i)^{-n}}

- **Onde**:
- PV: Valor financiado
- i: Taxa de juros mensal
- n: Número de parcelas




- **Cálculo por Parcela:**  
  - **Juros:**

   ```math
   \text{Juros} = \text{Saldo Devedor} \times i
   ```
   
  - **Amortização:**
   ```math
    \text{Amortização} = PMT - \text{Juros}

#

 ### Sistema SAC (Amortização Constante)
- **Amortização Mensal:**  
 ```math
\text{Amortização} = \frac{PV}{n}
```

- **Cálculo por Parcela:**  
  - **Juros:**
    ```math
    \text{Juros} = \text{Saldo Devedor} \times i
    
  - **Parcela:**
    ```math
    \text{Parcela} = \text{Amortização} + \text{Juros}

---

# 📝 Fluxo de Uso

## 1. Preenchimento dos Dados
Preencha os seguintes dados para realizar a simulação:
- **Dados do cliente**:
  - CPF
  - Nome completo
  - Renda mensal

- **Dados do financiamento**:
  - Valor do imóvel
  - Valor da entrada
  - Taxa de juros anual (%)
  - Prazo (em meses)

- **Seleção**:
  - Tipo de imóvel (residencial, comercial, etc.)
  - Sistema de amortização: `Price` ou `SAC`

---

## 2. Simulação
1. Clique no botão **"Calcular"** para processar a simulação.
2. O sistema irá:
   - Exibir o **valor total a pagar** (montante + juros).
   - Salvar automaticamente a simulação no banco de dados.

---

## 3. Resultados
Após o cálculo, você verá:
- Valor total financiado
- Total de juros
- Valor de cada parcela (de acordo com o sistema escolhido)
- Detalhes das parcelas (opcional, caso haja tabela expandível)

---

## 4. Consulta de Simulações Anteriores
Para buscar simulações salvas:
1. Insira o **CPF** do cliente no campo de busca.
2. Clique em **"Consultar"**.
3. O sistema exibirá todas as parcelas e condições de simulações anteriores vinculadas ao CPF.

---

## 🧮 Exemplo Prático

## 📋 Dados do Cliente
| Campo               | Valor               |
|---------------------|---------------------|
| CPF                 | 123.456.789-00      |
| Nome                | João Silva          |
| Renda Mensal        | R$ 5.000,00         |
| Valor do Imóvel     | R$ 300.000,00       |
| Entrada             | R$ 60.000,00        |
| Taxa Juros Anual    | 8,5%                |
| Prazo               | 360 meses (30 anos) |
| Sistema Amortização | SAC                 |

Valor financiado: R$ 240.000,00
Taxa mensal efetiva: 0,68%


Total a pagar: R$ 535507,09
Quantidade de parcelas: 360  

|  Mês  |  Parcela   |  Amortização  |   Juros   | Saldo Devedor  |
|-------|------------|---------------|-----------|----------------|
|   1   | R$ 2.303,83 | R$ 666,67     | R$ 1637,16| R$ 239.333,33 |
|   2   | R$ 2299,28 | R$ 666,67     | R$ 1632,61 | R$ 238.666,66 |
|  ...  |    ...     |      ...      |    ...     |       ...      |
|  360  | R$ 671,11  | R$ 666,67     | R$ 4,44    | R$ 0,00        |




### Dados de Entrada:
- **Valor do Imóvel:** R$ 300.000,00  
- **Entrada:** R$ 60.000,00  
- **Taxa de Juros Anual:** 10%  
- **Prazo:** 120 meses (10 anos)  
- **Renda Mensal do Cliente:** R$ 10.000,00  

---

# 📊 Detalhamento dos Cálculos (SAC)

## 📋 Dados do Cliente
| Campo               | Valor               |
|---------------------|---------------------|
| CPF                 | 123.456.789-00      |
| Nome                | João Silva          |
| Renda Mensal        | R$ 5.000,00         |
| Valor do Imóvel     | R$ 300.000,00       |
| Entrada             | R$ 60.000,00        |
| Taxa Juros Anual    | 8,5%                |
| Prazo               | 360 meses (30 anos) |
| Sistema Amortização | SAC                 |

---

## 🧮 Etapas do Cálculo

### 1. Valor Financiado (PV)
```math
PV = \text{Valor do Imóvel} - \text{Entrada} = 300.000 - 60.000 = R\$ 240.000,00
```

### 2. Taxa de Juros Mensal
```math
i_{\text{mensal}} = (1 + 0,085)^{1/12} - 1 \approx 0,68\% \text{ (ou 0,0068 em decimal)}
```

### 3. Amortização Constante
```math
\text{Amortização} = \frac{PV}{n} = \frac{240.000}{360} = R\$ 666,67 \text{ por mês}
```

### 4. Cálculo das Parcelas
- Primeira Parcela (Mês 1):

```math
\begin{align*}
\text{Juros}_1 &= PV \times i_{\text{mensal}} = 240.000 \times 0,0068 = R\$ 1.632,00 \\
\text{Parcela}_1 &= \text{Amortização} + \text{Juros}_1 = 666,67 + 1.632,00 = R\$ 2303,83 \\
\text{Saldo}_1 &= PV - \text{Amortização} = 240.000 - 666,67 = R\$ 239.333,33
\end{align*}
```

- Segunda Parcela (Mês 2):

```math
\begin{align*}
\text{Juros}_2 &= \text{Saldo}_1 \times i_{\text{mensal}} = 239.333,33 \times 0,0068 \approx R\$ 1.622,67 \\
\text{Parcela}_2 &= 666,67 + 1.622,67 = R\$ 2299,28 \\
\text{Saldo}_2 &= 239.333,33 - 666,67 = R\$ 238.666,67
\end{align*}
```

- Última Parcela (Mês 360):

```math
\begin{align*}
\text{Juros}_{360} &= \text{Saldo}_{359} \times i_{\text{mensal}} \approx 666,67 \times 0,0068 \approx R\$ 4,44 \\
\text{Parcela}_{360} &= 666,67 + 4,44 = R\$ 671,11 \\
\text{Saldo}_{360} &= 666,67 - 666,67 = R\$ 0,00
\end{align*}
```

### 5. Total a Pagar
```math
\text{Total} = \sum_{k=1}^{360} (\text{Amortização} + \text{Juros}_k) = 240.000 + 180.000 = R\$ 535507,09
```

---

## 🚀 Como Executar o Projeto

### Pré-requisitos:
- Java 17+
- MySQL (Configure um banco chamado `financiai`).
- Configurar credenciais do banco em `Conexao.java`.

### Passos:
1. Clone o repositório:
   ```bash
   git clone https://github.com/programaagibank/FinanciAi.git
