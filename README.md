# carbonJavaReporter

docker compose -f carbone.yml  up -d

curl --location 'http://10.10.10.61:9999/postTemplate' \
--form 'file=@"/home/user/Downloads/Document1.docx"'


{
"picture": "https://example.com/static/frame.png",
"title": "Отчёт по участку 123",
"value": "Описание участка",
"crs": "EPSG:7828",
"area": 12345.67,
"coords": [ ... ]
}
