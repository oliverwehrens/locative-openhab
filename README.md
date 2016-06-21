Locative-Server
===============

This is a small backend for Locative (https://itunes.apple.com/us/app/locative/id725198453?mt=8) and 
Openhab (http://openhab.org).

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

--openhabserver
--switchprefix
--geofenceuserfile


Example:

java -jar locative.jar --openhabserver=http://myopenhabserver.local:8080 --switchprefix=Anwesenheit --geofenceuserfile=/tmp/pw.txt

To run it with docker:

It is assumed that the password file is named user.pw and located under /locative. So you need to mount it e.g. locally and put the 
password file there. You need to tell the service where your openhab server is (with -e OPENHAB=...). Furterhmore it is assumed
that the openhab switch is named 'Presence_<location>_<username>'.

Example run:

```
docker build -t 'locative' .

docker run -v $PWD:/locative -e OPENHAB=http://openhab.local:8080 locative
```