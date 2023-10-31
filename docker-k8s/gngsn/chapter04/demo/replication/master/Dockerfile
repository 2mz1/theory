FROM mysql:8.0.33

COPY ./master/my.cnf /etc/mysql/my.cnf

COPY ../prepare.sh /docker-entrypoint-initdb.d
COPY ../init-data.sh /usr/local/bin/
COPY ../sql /sql
