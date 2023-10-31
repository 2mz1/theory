INSERT INTO todo(`title`, `content`, `status`, `created`, `updated`) VALUES
('MySQL Docker 이미지 생성','MySQL Master, Slave를 환경 변수로 설정할 수 있는 MySQL 이미지 생성', 'DONE', NOW(), NOW()),
('MySQL Stack 생성','MySQL Master, Slave를 환경 변수로 설정할 수 있는 MySQL 이미지 생성', 'DONE', NOW(), NOW()),
('APIを実装する','Go言語でTODOの参照・更新処理を行うためのAPIを実装する', 'PROGRESS', NOW(), NOW()),
('NginxのDockerイメージを作成する','バックエンドにHTTPリクエストを流すNginxのイメージを作成する', 'PROGRESS', NOW(), NOW()),
('APIのStackを構築する','NginxとAPIから成るスタックをSwarmクラスタに構築する', 'PROGRESS', NOW(), NOW()),
('Webを実装する','Nuxt.jsを使用して、APIと連携したTODOの状態を表示するWebアプリケーションを実装する', 'PROGRESS', NOW(), NOW()),
('WebのStackを構築する','NginxとWebから成るスタックをSwarmクラスタに構築する', 'PROGRESS', NOW(), NOW()),
('Ingressを構築する','Swarmクラスタに外からアクセスするためのIngressを構築する', 'TODO', NOW(), NOW());

