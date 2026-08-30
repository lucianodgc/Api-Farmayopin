FROM ubuntu:latest
LABEL authors="lugam"

ENTRYPOINT ["top", "-b"]