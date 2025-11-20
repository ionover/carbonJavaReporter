# carbonJavaReporter

docker compose -f carbone.yml  up -d

curl --location 'http://10.10.10.61:9999/postTemplate' \
--form 'file=@"/home/user/Downloads/Document1.docx"'