FROM java:8-jre

EXPOSE 9000

ADD build/libs/locative.jar /locative.jar
RUN mkdir /locative
VOLUME "/locative"
ENV OPENHAB=openhab
CMD ["java", "-jar", "/locative.jar", "--geofenceuserfile=/locative/user.pw", "--openhabserver=${OPENHAB}", "--switchprefix=Presence"]
