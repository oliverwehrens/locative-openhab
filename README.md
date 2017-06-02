Locative-Server
===============

Update:

2017-06-02 This is now compatible with OpenHab2 and uses it's REST API.

This is a small backend for Locative (https://itunes.apple.com/us/app/locative/id725198453?mt=8) and 
Openhab (http://openhab.org). The locative mobile apps are open source on github. 

- iOS: https://github.com/LocativeHQ/ios-app
- Android: https://github.com/LocativeHQ/android-app

The locative app can be used to trigger presence detection in openhab.

Locative supports Basic Auth to authenticate users. This information will be used to determine the user which will triggered.
Username and passwords are define in the ``geofenceuserfile`` switch with the following syntax:

```
<username>:<password>
```

e.g.

```
oliver:supersecretpasswort
```

The ``openhabserver`` switch defines the server where your openhab installation is running, e.g. ``http://openhab.local:8080``.

The switch which will be tried to use (in openhab) needs to be named ``switchprefix_locationid_user``. ``switchprefix`` will default to 'Presence'.
``locationid`` is the id you defined in Locative and ``user`` is the user from the above ``geofenceuserfile``. If a specified
location is entered the switch will be set to ON, if you leave it will be set to OFF. Locative get the corresponding HTTP return code
from openhab. If you send a test request or a request with any other trigger event you will ge a 400 BAD REQEUST code.

Command line parameter names:
```
--openhabserver
--switchprefix
--geofenceuserfile
```

Example:
```
java -jar locative.jar --openhabserver=http://myopenhabserver.local:8080 --switchprefix=Anwesenheit --geofenceuserfile=/tmp/pw.txt
```

To test it use:

```
curl -X POST -u oliver:pw 'http://localhost:9000?id=Home&trigger=enter'
```

This will send for the user oliver an ON to the item Presence_home_oliver in Openhab. 

If the location 'test' is detected Openhab will not be called and http code 200 will be returned.

To run it with docker:

It is assumed that the password file is named user.pw and located under /locative. So you need to mount it e.g. locally and put the 
password file there. You need to tell the service where your openhab server is (with -e OPENHAB=...). Furterhmore it is assumed
that the openhab switch is named 'Presence_<location>_<username>'.

Example run:

```
docker build -t 'locative' .

docker run -v $PWD:/locative -e OPENHAB=http://openhab.local:8080 locative
```
Running in docker, the server looks up the ``geofenceuserfile`` at /locative/user.pw. So put the file with that name in the volume specified with -v in the above line (e.g. current directory). ``switchprefix`` is also fixed at ``Presence`` when using docker (of course changing that is just a matter of modifing the ``Dockerfile``.

So the chain to trigger presence detection in your local openhab installation running at home could look like this:

```
+------------+    +--------------------------+    +--------------------------+    +---------+
| DSL Router | -> | Caddy with Let's encrypt | -> | locative/openhab gateway | -> | openhab |
+------------+    +--------------------------+    +--------------------------+    +---------+
```

DSL Router runs with a DynDNS provider and forwards port 443 to Caddy. Caddy forwards the request on a specific domain to the gateway. This gateway triggers openhab.

Oh yes, and it really is just under 100 lines of real code thanks to Spring Boot & friends. This can be less with php, ruby or python. I did this because I could not find a production ready image (aka Docker) thing which works.

Have fun.
