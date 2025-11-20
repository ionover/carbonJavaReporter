# carbonJavaReporter

docker compose -f carbone.yml  up -d

curl --location 'http://10.10.10.61:9999/postTemplate' \
--form 'file=@"/home/user/Downloads/Document1.docx"'

curl --location 'http://10.10.10.61:9999/save-me-pdf' \
--header 'Content-Type: application/json' \
--data '{
"templateId": "c4aa48638f600bb3da4892e19836bbc5a476698c8548077d751243f81cd3074b",
"d": {
"picture": "test.png",
"title": "test title",
"value": "test value",
"crs": "test crs",
"area": 123.45,
"coords": [
{
"num": 1,
"x": 1.0,
"y": 2.0
},
{
"num": 2,
"x": 3.0,
"y": 4.0
}
]
}
}
'