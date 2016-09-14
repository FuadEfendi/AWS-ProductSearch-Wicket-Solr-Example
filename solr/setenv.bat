set JAVA_OPTS=-Xms512m -Xmx512m
set JAVA_OPTS=%JAVA_OPTS% -Dfile.encoding=UTF8
set JAVA_OPTS=%JAVA_OPTS% -Duser.timezone=GMT
set JAVA_OPTS=%JAVA_OPTS% -Djava.net.preferIPv4Stack=true
set JAVA_OPTS=%JAVA_OPTS% -Dsolr.solr.home=/java/git/tokenizer-wicket/solr
set JAVA_OPTS=%JAVA_OPTS% -Dsolr.data.dir=/data/solr-4.10.0

REM JAVA_OPTS="$JAVA_OPTS -Dlog4j.configuration=file:/data/solr-4.10.0/log4j.properties"
