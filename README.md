# Brian's challenge

So first Time using Http4s :(, wouldn't mind using a lib I have more experience with; but it's a nice client.

## Deployment

This is a very small application, the deployment process could be dockerizing this jar/zip and just running it like you
would in the `start.sh` file or just publishing the jar/zip to a Kubernetes cluster, ec2; ecs.

This application was made with java 11 and scala 13 so if you would to recompile it.

### Required environment variable

| Variable             | Usage                                           |
|----------------------|-------------------------------------------------|
| HTTP_PORT            | port used in application host will be localhost |
| CSCARDS_ENDPOINT     | clear score base url                            |
| SCOREDCARDS_ENDPOINT | scored cards base url                           |
