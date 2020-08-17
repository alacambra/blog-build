docker build -t alacambra/s2i-nginx .
docker push alacambra/s2i-nginx
oc import-image s2i-nginx --from alacambra/s2i-nginx