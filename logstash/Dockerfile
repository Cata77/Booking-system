FROM docker.elastic.co/logstash/logstash:8.14.2

COPY postgresql-42.7.3.jar /usr/share/logstash/logstash-core/lib/jars/
COPY config/ /usr/share/logstash/config/
COPY queries/hotel_query.sql /usr/share/logstash/queries/

CMD ["logstash", "-f", "/usr/share/logstash/config/datasync.conf"]