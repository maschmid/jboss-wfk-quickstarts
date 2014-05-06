
This is a Quick and dirty port of the http://spring.io/guides/gs/messaging-stomp-websocket/ quickstart:

Protocol must be org.apache.coyote.http11.Http11NioProtocol.

e.g. this in standalone.xml:

 <connector name="http" protocol="org.apache.coyote.http11.Http11NioProtocol" scheme="http" socket-binding="http"/>


