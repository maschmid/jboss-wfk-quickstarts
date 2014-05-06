
Protocol must be org.apache.coyote.http11.Http11NioProtocol.

e.g. this in standalone.xml:

<connector name="http" protocol="org.apache.coyote.http11.Http11NioProtocol" scheme="http" socket-binding="http"/>


