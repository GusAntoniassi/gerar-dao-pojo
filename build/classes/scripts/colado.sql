CREATE DATABASE 'D:\Carlos\Curso de Java 2016\BANCO.FDB' user 'SYSDBA' password 'masterkey';

CREATE DOMAIN SIM_NAO AS CHAR(1) NOT NULL CHECK (VALUE IN ('S', 'N'));
CREATE DOMAIN FISICA_JURIDICA AS CHAR(1) NOT NULL CHECK (VALUE IN ('F', 'J'));
CREATE DOMAIN PEDIDO_COMPRA AS CHAR(2) NOT NULL CHECK (VALUE IN ('PA', 'PF', 'CA', 'CF'));
CREATE DOMAIN ABERTO_FECHADO AS CHAR(1) NOT NULL CHECK (VALUE IN ('A', 'F'));
CREATE DOMAIN DEBITO_CREDITO AS CHAR(1) NOT NULL CHECK (VALUE IN ('D', 'C'));

CREATE TABLE FJIASF (

);

CREATE TABLE PAIS (
    ID                    INTEGER          NOT NULL   PRIMARY KEY,
    NOME                  VARCHAR(50)      NOT NULL,
    SIGLA                 CHAR(3)          NOT NULL,
    ATIVO                 SIM_NAO          NOT NULL
);

CREATE TABLE ESTADO (
    ID                    INTEGER          NOT NULL   PRIMARY KEY,
    NOME                  VARCHAR(50)      NOT NULL,
    SIGLA                 CHAR(3)          NOT NULL,
    ATIVO                 SIM_NAO          NOT NULL,
    IDPAIS                INTEGER          NOT NULL,
    FOREIGN KEY (IDPAIS) REFERENCES PAIS (ID)
);

CREATE TABLE CIDADE (
    ID                    INTEGER          NOT NULL   PRIMARY KEY,
    NOME                  VARCHAR(50)      NOT NULL,
    ATIVO                 SIM_NAO          NOT NULL,
    IDESTADO              INTEGER          NOT NULL,
    FOREIGN KEY (IDESTADO) REFERENCES ESTADO (ID)
);

CREATE TABLE PESSOA (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    NOMERAZAOSOCIAL       VARCHAR(50)      NOT NULL,
    APELIDONOMEFANTASIA   VARCHAR(50),
    ENDERECO              VARCHAR(50)      NOT NULL,
    COMPLEMENTO           VARCHAR(50),
    BAIRRO                VARCHAR(50)      NOT NULL,
    CEP                   CHAR(10)         NOT NULL,
    TELEFONE1             VARCHAR(17)      NOT NULL,
    TELEFONE2             VARCHAR(17),
    CPFCNPJ               VARCHAR(18),
    RGIE                  VARCHAR(20),
    CLIENTE               SIM_NAO          NOT NULL,
    FORNECEDOR            SIM_NAO          NOT NULL,
    TIPOPESSOA            FISICA_JURIDICA  NOT NULL,
    ATIVO                 SIM_NAO          NOT NULL,
    IDCIDADE              INTEGER          NOT NULL,
    FOREIGN KEY (IDCIDADE) REFERENCES CIDADE (ID)
);

CREATE TABLE PRODUTO (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    NOME                  VARCHAR(50)      NOT NULL,
    QUANTIDADE            INTEGER          NOT NULL,
    PRECOVENDA            NUMERIC(18, 2)   NOT NULL,
    PRECOCOMPRA           NUMERIC(18, 2)   NOT NULL,
    ATIVO                 SIM_NAO          NOT NULL
);

CREATE TABLE COMPRA (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    DATA                  DATE             NOT NULL,
    TOTAL                 NUMERIC(18, 2)   NOT NULL,
    DESCONTO              NUMERIC(18, 2)   NOT NULL,
    LIQUIDO               NUMERIC(18, 2)   NOT NULL,
    STATUS                PEDIDO_COMPRA, /* Primeiro caractere: P = pedido ou C = compra. Segundo caractere: A = aberto ou F = fechado */
    IDPESSOA              INTEGER          NOT NULL,
    FOREIGN KEY (IDPESSOA) REFERENCES PESSOA (ID)
);

CREATE TABLE ITEMCOMPRA (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    QUANTIDADE            INTEGER          NOT NULL,
    UNITARIO              NUMERIC(18, 2)   NOT NULL,
    DESCONTO              NUMERIC(18, 2)   NOT NULL,
    LIQUIDO               NUMERIC(18, 2)   NOT NULL,
    IDCOMPRA              INTEGER          NOT NULL,
    IDPRODUTO             INTEGER          NOT NULL,
    FOREIGN KEY (IDCOMPRA) REFERENCES COMPRA (ID),
    FOREIGN KEY (IDPRODUTO) REFERENCES PRODUTO (ID)
);

CREATE TABLE CONTAPAGAR (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    DATA                  DATE             NOT NULL,
    VALOR                 NUMERIC(18, 2)   NOT NULL,
    QUITADA               SIM_NAO          NOT NULL,
    IDPESSOA              INTEGER          NOT NULL,
    IDCOMPRA              INTEGER,
    FOREIGN KEY (IDPESSOA) REFERENCES PESSOA (ID),
    FOREIGN KEY (IDCOMPRA) REFERENCES COMPRA (ID)
);

CREATE TABLE PARCELACONTAPAGAR (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    VENCIMENTO            DATE             NOT NULL,
    VALOR                 NUMERIC(18, 2)   NOT NULL,
    VALORPAGO             NUMERIC(18, 2)   NOT NULL,
    QUITADA               SIM_NAO          NOT NULL,
    IDCONTAPAGAR          INTEGER          NOT NULL,
    FOREIGN KEY (IDCONTAPAGAR) REFERENCES CONTAPAGAR (ID)
);

CREATE TABLE PAGAMENTO (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    DATA                  DATE             NOT NULL,
    VALOR                 NUMERIC(18, 2)   NOT NULL,
    DESCONTO              NUMERIC(18, 2)   NOT NULL,
    MULTA                 NUMERIC(18, 2)   NOT NULL,
    JUROS                 NUMERIC(18, 2)   NOT NULL,
    IDPARCELACONTAPAGAR INTEGER          NOT NULL,
    FOREIGN KEY (IDPARCELACONTAPAGAR) REFERENCES PARCELACONTAPAGAR (ID)
);

CREATE TABLE VENDA (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    DATA                  DATE             NOT NULL,
    TOTAL                 NUMERIC(18, 2)   NOT NULL,
    DESCONTO              NUMERIC(18, 2)   NOT NULL,
    LIQUIDO               NUMERIC(18, 2)   NOT NULL,
    STATUS                PEDIDO_COMPRA, /* Primeiro caractere: P = pedido ou C = compra. Segundo caractere: A = aberto ou F = fechado */
    IDPESSOA              INTEGER          NOT NULL,
    FOREIGN KEY (IDPESSOA) REFERENCES PESSOA (ID)
);



CREATE TABLE ITEMVENDA (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    QUANTIDADE            INTEGER          NOT NULL,
    UNITARIO              NUMERIC(18, 2)   NOT NULL,
    DESCONTO              NUMERIC(18, 2)   NOT NULL,
    LIQUIDO               NUMERIC(18, 2)   NOT NULL,
    IDVENDA               INTEGER          NOT NULL,
    IDPRODUTO             INTEGER          NOT NULL,
    FOREIGN KEY (IDVENDA) REFERENCES VENDA (ID),
    FOREIGN KEY (IDPRODUTO) REFERENCES PRODUTO (ID)
);

CREATE TABLE CONTARECEBER (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    DATA                  DATE             NOT NULL,
    VALOR                 NUMERIC(18, 2)   NOT NULL,
    QUITADA               SIM_NAO          NOT NULL,
    IDPESSOA              INTEGER          NOT NULL,
    IDVENDA               INTEGER,
    FOREIGN KEY (IDPESSOA) REFERENCES PESSOA (ID),
    FOREIGN KEY (IDVENDA)  REFERENCES VENDA (ID)
);

CREATE TABLE PARCELACONTARECEBER (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    VENCIMENTO            DATE             NOT NULL,
    VALOR                 NUMERIC(18, 2)   NOT NULL,
    VALORPAGO             NUMERIC(18, 2)   NOT NULL,
    QUITADA               SIM_NAO          NOT NULL,
    IDCONTARECEBER        INTEGER          NOT NULL,
    FOREIGN KEY (IDCONTARECEBER) REFERENCES CONTARECEBER (ID)
);

CREATE TABLE RECEBIMENTO (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    DATA                  DATE             NOT NULL,
    VALOR                 NUMERIC(18, 2)   NOT NULL,
    DESCONTO              NUMERIC(18, 2)   NOT NULL,
    MULTA                 NUMERIC(18, 2)   NOT NULL,
    JUROS                 NUMERIC(18, 2)   NOT NULL,
    IDPARCELACONTARECEBER INTEGER          NOT NULL,
    FOREIGN KEY (IDPARCELACONTARECEBER) REFERENCES PARCELACONTARECEBER (ID)
);

CREATE TABLE CAIXA (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    DATA                  DATE             NOT NULL,
    SALDOINICIAL          NUMERIC(18, 2)   NOT NULL,
    TOTALENTRADAS         NUMERIC(18, 2)   NOT NULL,
    TOTALSAIDAS           NUMERIC(18, 2)   NOT NULL,
    SALDOFINAL            NUMERIC(18, 2)   NOT NULL,
    DIFERENCA             NUMERIC(18, 2)   NOT NULL,
    STATUS                ABERTO_FECHADO /* A = Aberto ou F = Fechado */
);

CREATE TABLE MOVIMENTOCAIXA (
    ID                    INTEGER          NOT NULL  PRIMARY KEY,
    HISTORICO             VARCHAR(200)     NOT NULL,
    TIPO                  DEBITO_CREDITO, /* D = Débito ou C = Crédito */
    VALOR                 NUMERIC(18, 2)   NOT NULL,
    IDCAIXA               INTEGER          NOT NULL,
    IDPAGAMENTO           INTEGER,
    IDRECEBIMENTO         INTEGER,
    FOREIGN KEY (IDCAIXA) REFERENCES CAIXA (ID),
    FOREIGN KEY (IDPAGAMENTO) REFERENCES PAGAMENTO (ID),
    FOREIGN KEY (IDRECEBIMENTO) REFERENCES RECEBIMENTO (ID)
);

CREATE GENERATOR gen_pais;
CREATE GENERATOR gen_estado;
CREATE GENERATOR gen_cidade;
CREATE GENERATOR gen_pessoa;
CREATE GENERATOR gen_produto;
CREATE GENERATOR gen_compra;
CREATE GENERATOR gen_itemcompra;
CREATE GENERATOR gen_contapagar;
CREATE GENERATOR gen_parcelacontapagar;
CREATE GENERATOR gen_pagamento;
CREATE GENERATOR gen_venda;
CREATE GENERATOR gen_itemvenda;
CREATE GENERATOR gen_contareceber;
CREATE GENERATOR gen_parcelacontareceber;
CREATE GENERATOR gen_recebimento;
CREATE GENERATOR gen_caixa;
CREATE GENERATOR gen_movimentocaixa;

/* Inserir alguns países */
INSERT INTO PAIS VALUES (1, 'Brasil', 'BRA', 'S');
INSERT INTO PAIS VALUES (2, 'Paraguai', 'PRY', 'S');
INSERT INTO PAIS VALUES (3, 'Argentina', 'ARG', 'N');
SET GENERATOR GEN_PAIS TO 3;

/* Inserir alguns estados */
/* Brasil */
INSERT INTO ESTADO VALUES (1, 'Paraná', 'PR', 'S', 1);
INSERT INTO ESTADO VALUES (2, 'São Paulo', 'SP', 'S', 1);
INSERT INTO ESTADO VALUES (3, 'Rio de Janeiro', 'RJ', 'N', 1);
INSERT INTO ESTADO VALUES (4, 'Santa Catarina', 'SC', 'S', 1);
/* Paraguai */
INSERT INTO ESTADO VALUES (5, 'Alto Paraguay', 'AP', 'S', 2);
INSERT INTO ESTADO VALUES (6, 'Boquerón', 'BQ', 'S', 2);
/* Argentina */
INSERT INTO ESTADO VALUES (7, 'La Pampa', 'LP', 'S', 3);
SET GENERATOR GEN_ESTADO TO 7;

/* Inserir algumas cidades */
/* Paraná */
INSERT INTO CIDADE VALUES (1, 'Umuarama', 'S', 1);
INSERT INTO CIDADE VALUES (2, 'Cascavel', 'N', 1);
INSERT INTO CIDADE VALUES (3, 'São Manoel do Paraná', 'N', 1);
/* São Paulo */
INSERT INTO CIDADE VALUES (4, 'São Paulo', 'S', 2);
INSERT INTO CIDADE VALUES (5, 'São Bernardo do Campo', 'S', 2);
/* Santa Catarina */
INSERT INTO CIDADE VALUES (6, 'Florianópolis', 'S', 4);
/* Alto Paraguay (PRY) */
INSERT INTO CIDADE VALUES (7, 'Fuerte Olimpo', 'S', 5);
INSERT INTO CIDADE VALUES (8, 'Capitán Pablo Lagerenza', 'S', 5);
/* Boquerón (PRY) */
INSERT INTO CIDADE VALUES (9, 'Loma Plata', 'S', 6);
INSERT INTO CIDADE VALUES (10, 'Filadelfia', 'S', 5);
/* La Pampa (ARG) */
INSERT INTO CIDADE VALUES (11, 'Santa Rosa', 'S', 7);
SET GENERATOR GEN_CIDADE TO 11;

/* Inserir algumas pessoas */
INSERT INTO PESSOA VALUES (1, 'João da Silva', 'Joãozinho', 'R. das Flores, 3375', 'Fundo', 'Zona XI', '09780-050',
                              '+55(11)99953-1818', null, '577.149.240-04', '18.290.977-3', 'S', 'N', 'F', 'S', 5);
                                    
INSERT INTO PESSOA VALUES (2, 'Maria Rodrigues', null, 'Praça Paraná, 50', null, 'Centro', '87.215-000',
                              '+55(44)3644-1114', null, '203.995.744-84', '44.496.607-9', 'S', 'N', 'F', 'S', 3);
                                    
INSERT INTO PESSOA VALUES (3, 'J. Martins Atacado', 'Supermercados Planalto', 'Av. Rolândia, 4000', null, 'Centro', '87503-000',
                              '+55(44)3621-3100', null, '17.458.187/0001-74', '589.91087-70', 'N', 'S', 'J', 'S', 1);
                                    
INSERT INTO PESSOA VALUES (4, 'Lihon & Sung Comércio de Utilidades', 'Shopping China', 'Ruta Bahia Negra, 2121', null, 'Obelisco', '80900-000',
                              '+595(83)9752-2440', null, '18.026.994/0001-80', '315.89448-46', 'N', 'S', 'J', 'S', 7);
                                    
INSERT INTO PESSOA VALUES (5, 'Elzira', 'Dona Elzira', 'Rua Marialva, 4875', null, 'Zona III', '87502-100',
                              '+55(44)3623-3958', null, '203.995.744-84', '39.684.693-2', 'S', 'S', 'F', 'S', 1);
SET GENERATOR GEN_PESSOA TO 5;

/* Inserir alguns produtos */
INSERT INTO PRODUTO VALUES (1, '', 1, 1, 1, 'S');
INSERT INTO PRODUTO VALUES (2, 'Budweiser 343ml - Caixa com 6 unidades', 100, 19.14, 12.00, 'S');
INSERT INTO PRODUTO VALUES (3, 'Patagonia Bohemian - Long Neck 355ml Un.', 20, 9.50, 3.50, 'S');
INSERT INTO PRODUTO VALUES (4, 'Stella Artois 275ml - Caixa com 6 unidades', 95, 20.94, 14.00, 'S');
INSERT INTO PRODUTO VALUES (5, 'Original 300ml - Caixa com 12 unidades', 90, 41.88, 28.76, 'S');
INSERT INTO PRODUTO VALUES (6, 'Skol Beats Senses 313ml - Caixa com 6 unidades', 300, 29.94, 12.88, 'S');
INSERT INTO PRODUTO VALUES (7, 'Balde de Gelo Corona 5 Litros', 70, 89.90, 32.00, 'S');
INSERT INTO PRODUTO VALUES (8, 'Copo Budweiser 400ml - 1 Unidade', 100, 19.90, 5.00, 'S');
SET GENERATOR GEN_PRODUTO TO 8;
