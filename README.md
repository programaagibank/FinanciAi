# 🏠 FinanciAí

**FinanciAí** é um simulador de financiamento imobiliário prático e versátil, desenvolvido para oferecer ao usuário uma experiência completa na visualização de condições de financiamento nos sistemas Price e SAC. O sistema calcula parcelas, armazena simulações e permite consultas futuras via CPF.

---

## 📌 Descrição do Projeto

O **FinanciAí** foi criado para **simular financiamentos imobiliários** com dois sistemas de amortização:  
- **Price**: Parcelas constantes com juros decrescentes.  
- **SAC**: Amortização constante com parcelas decrescentes.  

O sistema armazena todas as simulações em um banco de dados MySQL, permitindo consultas rápidas e gerenciamento de histórico.

---

## ⚙️ Funcionalidades

- **📊 Simulação de Financiamento**  
  - Cálculo automático de parcelas (Price/SAC).  
  - Detalhamento de amortização, juros e saldo devedor.  
- **🔍 Consulta por CPF**  
  - Recuperação de simulações salvas.  
- **💾 Armazenamento em Banco de Dados**  
  - Persistência de clientes, imóveis, financiamentos e parcelas.  

---

## 🛠️ Tecnologias Utilizadas

## 🛠️ Tecnologias Utilizadas

- **Java**  
  Linguagem principal do projeto, com interface gráfica desenvolvida em **JavaFX**.

- **MySQL**  
  Banco de dados relacional utilizado para armazenar as simulações dos usuários.

- **IntelliJ IDEA**  
  IDE escolhida para o desenvolvimento do código.

- **Jira**  
  Organização, divisão e acompanhamento de tarefas durante o desenvolvimento.
  
- **Gradle**  
  Ferramenta de automação utilizada para gerenciamento de dependências e build do projeto.

- **GitHub**  
  Versionamento do código e controle colaborativo do projeto.

---

## 📖 Fórmulas Matemáticas

### Sistema Price (Parcelas Constantes)
- **Valor da Parcela (PMT):**  
  \[
  PMT = PV \times \frac{i}{1 - (1 + i)^{-n}}
  \]  
  *Onde:*  
  - \( PV \): Valor financiado  
  - \( i \): Taxa de juros mensal  
  - \( n \): Número de parcelas  

- **Cálculo por Parcela:**  
  - **Juros:** \( \text{Saldo Devedor} \times i \)  
  - **Amortização:** \( PMT - \text{Juros} \)  

### Sistema SAC (Amortização Constante)
- **Amortização Mensal:**  
  \[
  \text{Amortização} = \frac{PV}{n}
  \]  
- **Cálculo por Parcela:**  
  - **Juros:** \( \text{Saldo Devedor} \times i \)  
  - **Parcela:** \( \text{Amortização} + \text{Juros} \)  

---

## 🧮 Exemplo Prático

### Dados de Entrada:
- Valor do Imóvel: R$ 300.000,00  
- Entrada: R$ 60.000,00  
- Taxa de Juros Anual: 10%  
- Prazo: 120 meses (10 anos)  

### Cálculos:

#### Sistema Price:
1. **Taxa Mensal:**  
   \( (1 + 0.10)^{1/12} - 1 \approx 0.797\% \)  
2. **Valor Financiado (PV):** R$ 240.000,00  
3. **Parcela (PMT):**  
   \[
   PMT = 240000 \times \frac{0.00797}{1 - (1 + 0.00797)^{-120}} \approx R\$ 3.216,47
   \]  

#### Sistema SAC:
1. **Amortização Mensal:**  
   \( \frac{240000}{120} = R\$ 2.000,00 \)  
2. **Primeira Parcela:**  
   - Juros: \( 240000 \times 0.00797 = R\$ 1.912,80 \)  
   - Parcela: \( 2000 + 1912,80 = R\$ 3.912,80 \)  
3. **Última Parcela:**  
   - Juros: \( 2000 \times 0.00797 = R\$ 15,94 \)  
   - Parcela: \( 2000 + 15,94 = R\$ 2.015,94 \)  

---

## 🚀 Como Executar o Projeto

### Pré-requisitos:
- Java 17+
- MySQL (Configure um banco chamado `financiai`).
- Configurar credenciais do banco em `Conexao.java`.

### Passos:
1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/financiai.git
