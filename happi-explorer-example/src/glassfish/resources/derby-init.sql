
CREATE TABLE application_user(id VARCHAR(36) NOT NULL)
ALTER TABLE application_user ADD version BIGINT NOT NULL DEFAULT 0
ALTER TABLE application_user ADD username VARCHAR(255) NOT NULL DEFAULT '<null>'
ALTER TABLE application_user ADD password VARCHAR(512)
ALTER TABLE application_user ADD PRIMARY KEY(id)
ALTER TABLE application_user ADD UNIQUE(username)

CREATE TABLE application_user_role(user_id VARCHAR(36) NOT NULL, role VARCHAR(255) NOT NULL)
ALTER TABLE application_user_role ADD PRIMARY KEY(user_id, role)
ALTER TABLE application_user_role ADD FOREIGN KEY(user_id) REFERENCES application_user(id)

CREATE TABLE gender(id VARCHAR(36) NOT NULL)
ALTER TABLE gender ADD version BIGINT NOT NULL DEFAULT 0
ALTER TABLE gender ADD code VARCHAR(10) NOT NULL DEFAULT '<null>'
ALTER TABLE gender ADD name VARCHAR(255) NOT NULL DEFAULT '<null>'
ALTER TABLE gender ADD description VARCHAR(255)
ALTER TABLE gender ADD PRIMARY KEY(id)
ALTER TABLE gender ADD UNIQUE(code)

CREATE TABLE device(id VARCHAR(36) NOT NULL, gender_id VARCHAR(36) NOT NULL)
ALTER TABLE device ADD version BIGINT NOT NULL  DEFAULT 0
ALTER TABLE device ADD name VARCHAR(255) NOT NULL DEFAULT '<null>'
ALTER TABLE device ADD location VARCHAR(255) NOT NULL DEFAULT '<null>'
ALTER TABLE device ADD PRIMARY KEY(id)


CREATE TABLE customer(id VARCHAR(36) NOT NULL, gender_id VARCHAR(36) NOT NULL)
ALTER TABLE customer ADD version BIGINT NOT NULL  DEFAULT 0
ALTER TABLE customer ADD given_name VARCHAR(255) NOT NULL DEFAULT '<null>'
ALTER TABLE customer ADD family_name VARCHAR(255) NOT NULL DEFAULT '<null>'
ALTER TABLE customer ADD email VARCHAR(255) NOT NULL DEFAULT '<null>'
ALTER TABLE customer ADD phone_number VARCHAR(255)
ALTER TABLE customer ADD PRIMARY KEY(id)
ALTER TABLE customer ADD FOREIGN KEY(gender_id) REFERENCES gender(id)
ALTER TABLE customer ADD UNIQUE(email)

INSERT INTO gender(id, version, name, code, description) VALUES ('09ee5d9d-bf9b-4b5d-aad0-19117eb8da34', 1, 'Male', 'M', 'A male humain')
INSERT INTO gender(id, version, name, code, description) VALUES ('337ac663-48da-4a97-ad55-062a2c2ebb6d', 1, 'Female', 'F', 'A female humain')
INSERT INTO gender(id, version, name, code, description) VALUES ('4de35672-7934-4dc5-9dd4-33b5b65c70ea', 1, 'Hermaphrodite', 'H', 'A human with both sex')
INSERT INTO gender(id, version, name, code, description) VALUES ('3310026a-a69a-422a-a987-c0556484b331', 1, 'Asexual', 'A', 'A human without sex')

INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('0d5be73c-55f4-4379-accb-a7dcef0e9f2d', 1, 'John', 'DOE', 'john.doe@example.com', '0091077640', '09ee5d9d-bf9b-4b5d-aad0-19117eb8da34')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('6eb7cd4a-dbb4-4a7e-9bce-e3c959739c53', 1, 'Jane', 'DOE', 'jane.doe@example.com', '0052866577', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('b5d9a517-cd73-4643-adc5-c34ac7522791', 1, 'Baby', 'DOE', 'baby.doe@example.com', NULL, '09ee5d9d-bf9b-4b5d-aad0-19117eb8da34')

INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('8daa6a5c-690a-4afc-8993-d5bffc0e226c', 1, 'Ada', 'MINT', 'ada.mint@linux.org', '5464027137', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('fc79910d-282b-4ef5-925d-82ba77bdeeaa', 1, 'Barbara', 'MINT', 'barbara.mint@linux.org', '3918400808', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('9ebfa9da-c7a9-488e-96a6-bbad13f713d5', 1, 'Bea', 'MINT', 'bea.mint@linux.org', '2517187280', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('4f61ac74-3a40-4849-9d89-6622d16e4c98', 1, 'Bianca', 'MINT', 'bianca.mint@linux.org', '5053665015', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('daf2f827-c911-41c8-8f3e-b3239f72a192', 1, 'Cassandra', 'MINT', 'cassandra.mint@linux.org', '1694052784', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('8863733c-cdc7-417a-ae39-ac930ddbb827', 1, 'Celena', 'MINT', 'celena.mint@linux.org', '9858155310', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('55e633c2-18fb-44cd-a159-1e0b91ec9d65', 1, 'Daryna', 'MINT', 'daryna.mint@linux.org', '2334913560', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('5faa253d-e36e-4754-a946-253d9d89039d', 1, 'Elyssa', 'MINT', 'elyssa.mint@linux.org', '6301164809', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('b2de58b3-76e5-4529-be0f-7534cbb3f6e8', 1, 'Felicia', 'MINT', 'felicia.mint@linux.org', '9108205266', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('3e1dabe4-f4a0-40ab-aa0b-e539368e51bd', 1, 'Gloria', 'MINT', 'gloria.mint@linux.org', '4623770043', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('1422f2f5-f9d8-4cf8-be84-d5dd9bf9eac4', 1, 'Helena', 'MINT', 'helena.mint@linux.org', '4673806642', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('0b7f6663-5af4-46f7-80fd-d146a20801b0', 1, 'Isadora', 'MINT', 'isadora.mint@linux.org', '8832329787', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('48822907-b9c0-4a71-8e15-ec10ad860e5a', 1, 'Julia', 'MINT', 'julia.mint@linux.org', '8934789796', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('36ce9e2c-8d44-4c46-8afc-1a65bf48bbc7', 1, 'Katya', 'MINT', 'katya.mint@linux.org', '2275257929', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('32a9909b-e668-42a3-9588-acbf6fd2f369', 1, 'Lisa', 'MINT', 'lisa.mint@linux.org', '7403399762', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('8c909891-a5a8-4996-9c12-951a2618db99', 1, 'Maya', 'MINT', 'maya.mint@linux.org', '4266223404', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('0f936a14-48a3-4d33-821e-77e8bde58c65', 1, 'Nadia', 'MINT', 'nadia.mint@linux.org', '1991114004', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('3c94be17-2c95-44d9-be36-a7fd7dd6a5b9', 1, 'Olivia', 'MINT', 'olivia.mint@linux.org', '3095405260', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('fc69f541-2c60-44fe-ad74-76007760ff62', 1, 'Petra', 'MINT', 'petra.mint@linux.org', '7957678189', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('705d82ff-8262-41fd-a89e-256b3d72712e', 1, 'Qiana', 'MINT', 'qiana.mint@linux.org', '3027329678', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('d9258bcb-89cd-4c43-8dcb-326da8b44e23', 1, 'Rebecca', 'MINT', 'rebecca.mint@linux.org', '8048862801', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('96d0c440-08c2-4843-8c8f-5cf099cd2980', 1, 'Rafaela', 'MINT', 'rafaela.mint@linux.org', '4734881735', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('1cc3b96d-7a5f-4aaf-a70a-abb24a0074e0', 1, 'Rosa', 'MINT', 'rosa.mint@linux.org', '5762177108', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('d55d6089-0bdd-465f-835d-7f2776f4491e', 1, 'Sarah', 'MINT', 'sarah.mint@linux.org', '2090818583', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('5918cf27-194a-419f-b5e2-5ebb375c0e70', 1, 'Serena', 'MINT', 'serena.mint@linux.org', '3444404738', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('145001dd-c75d-4430-b5ed-addb15e9d13f', 1, 'Sonya', 'MINT', 'sonya.mint@linux.org', '2120834802', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('acf98409-b603-4bd3-abc2-7a242ed9366a', 1, 'Sylvia', 'MINT', 'sylvia.mint@linux.org', '5395579524', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('87c6178c-8b12-45a1-8364-00d9c8d5c806', 1, 'Tara', 'MINT', 'tara.mint@linux.org', '5031697400', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('0030171c-e829-4ef2-ac41-622f4dd215ba', 1, 'Tessa', 'MINT', 'tessa.mint@linux.org', '2397830442', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('9161954f-6ba2-458f-959a-567a8ca5e74c', 1, 'Tina', 'MINT', 'tina.mint@linux.org', '8917374827', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('000ebd8e-fd65-4ba5-96fd-b497af998593', 1, 'Tricia', 'MINT', 'tricia.mint@linux.org', '3625343059', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('f9881d49-d6b0-4cc3-8668-7c0570811ef2', 1, 'Ulyana', 'MINT', 'ulyana.mint@linux.org', '7326820353', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('da35473a-8137-43f5-aff5-d1d958af5687', 1, 'Ulyssa', 'MINT', 'ulyssa.mint@linux.org', '2201178600', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('d24df17e-a8b2-4402-bc37-e2908cb46d5e', 1, 'Uma', 'MINT', 'uma.mint@linux.org', '5460793830', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('f048853a-9b11-47ef-84da-12e7f253f6a9', 1, 'Una', 'MINT', 'una.mint@linux.org', '6519706319', '337ac663-48da-4a97-ad55-062a2c2ebb6d')
INSERT INTO customer(id, version, given_name, family_name, email, phone_number, gender_id) VALUES ('04c67cde-8b98-41a4-a743-fc8f74e4db23', 1, 'Vanessa', 'MINT', 'vanessa.mint@linux.org', '3179511991', '337ac663-48da-4a97-ad55-062a2c2ebb6d')

