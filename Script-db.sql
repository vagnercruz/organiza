create table usuarios(
	id SERIAL primary key,
	nome varchar(100),
	email varchar(100) unique,
	senha varchar(100),
	nome_usuario varchar(50) UNIQUE NOT NULL DEFAULT
);

CREATE TABLE transacoes (
	id SERIAL PRIMARY KEY,
	usuario_id INT REFERENCES usuarios(id),
	tipo VARCHAR(10),
	descricao VARCHAR(255),
	valor DECIMAL(10,2),
	data_transacao DATE,
	transacoes varchar (100) NOT null,
	observacao TEXT,
	recorrente BOOLEAN DEFAULT FALSE,
	frequencia VARCHAR(20);
);

CREATE TABLE contas (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id INT NOT NULL,
    nome VARCHAR(100),
    tipo VARCHAR(50),
    saldo_inicial DOUBLE PRECISION,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

