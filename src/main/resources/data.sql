-- ============================================
-- Sistema de Evento Esportivo - Dados Iniciais
-- ============================================

-- ============================================
-- ADMIN (senha: admin123 com BCrypt)
-- ============================================
INSERT INTO usuarios (nome, login, senha, tipo) VALUES
('Administrador', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN');

-- ============================================
-- ESTÁDIOS (Sedes reais de 2026)
-- ============================================
INSERT INTO estadios (id, nome, capacidade) VALUES
(1, 'Estadio Azteca', 83264),
(2, 'MetLife Stadium', 82500),
(3, 'SoFi Stadium', 70240),
(4, 'BMO Field', 30000),
(5, 'Hard Rock Stadium', 64767),
(6, 'AT&T Stadium', 80000);

-- ============================================
-- CIDADES-SEDE
-- ============================================
INSERT INTO cidades_sede (id, nome, pais, descricao, estadio_id) VALUES
(1, 'Cidade do México', 'México', 'A histórica capital mexicana. O Azteca será o primeiro estádio a sediar três Copas do Mundo.', 1),
(2, 'Nova York / Nova Jersey', 'Estados Unidos', 'A grande metrópole americana sediará a grande final da Copa de 2026.', 2),
(3, 'Los Angeles', 'Estados Unidos', 'A capital do entretenimento. O moderníssimo SoFi Stadium será um dos principais palcos.', 3),
(4, 'Toronto', 'Canadá', 'A maior cidade do Canadá fará sua estreia como anfitriã em Copas do Mundo.', 4),
(5, 'Miami', 'Estados Unidos', 'Conhecida pelo clima tropical e forte influência latina, Miami é um dos pólos do futebol nos EUA.', 5),
(6, 'Dallas', 'Estados Unidos', 'Sede do colossal AT&T Stadium, receberá o maior número de jogos do torneio.', 6);

-- ============================================
-- HOTÉIS
-- ============================================
INSERT INTO hoteis (nome, estrelas, endereco, cidade_id) VALUES
('Four Seasons CDMX', 5, 'Paseo de la Reforma, 500', 1),
('St. Regis Mexico City', 5, 'Paseo de la Reforma, 439', 1),
('The Plaza Hotel', 5, '768 5th Ave, Nova York', 2),
('Hilton Meadowlands', 4, '2 Meadowlands Plaza, NJ', 2),
('The Beverly Hills Hotel', 5, '9641 Sunset Blvd, LA', 3),
('Fairmont Royal York', 5, '100 Front St W, Toronto', 4),
('Fontainebleau Miami Beach', 5, '4441 Collins Ave, Miami', 5),
('Omni Dallas Hotel', 4, '555 S Lamar St, Dallas', 6);

-- ============================================
-- AEROPORTOS
-- ============================================
INSERT INTO aeroportos (nome, codigo, cidade_id) VALUES
('Aeroporto Int. da Cidade do México', 'MEX', 1),
('Aeroporto Felipe Ángeles', 'NLU', 1),
('John F. Kennedy Int. Airport', 'JFK', 2),
('Newark Liberty Int. Airport', 'EWR', 2),
('Los Angeles Int. Airport', 'LAX', 3),
('Toronto Pearson Int. Airport', 'YYZ', 4),
('Miami Int. Airport', 'MIA', 5),
('Dallas/Fort Worth Int. Airport', 'DFW', 6);

-- O restante dos dados (Seleções, Partidas, Chaveamentos) será importado automaticamente da API!
