# udd04
Upravljanje digitalnim dokumentima elasticsearch

Korisiti se Elasticsearch database za skladistenje indeksa.
Kibana se korsti za pristup bazi Elasticsearch.
U projektu se vrsi popunjvanje prijave za posao(popunjavaju se osnovna polja i upload-uje cv koji je u pdf formatu)i vrsi se indeksiranje.
Funkcionalnosti:
1. popunjvanje prijave za posao(popunjavaju se osnovna polja i upload-uje cv koji je u pdf formatu) i vrsi se indeksiranje
2. simple search (indeksi mogu pretrazivati po svakom polju)
3. advanced search (kombinovana pretraga sa and,or i not operatorima)
4. phraze search (omoguceno je yadavanje fraze kod kombinovane pretrage)
5. geolocation search (unosi grad, drzava, i radius u okviru kojeg ce se traziti aplikanti)
6. statistika (prikazuje se grad i vreme odakle i kad je popunjeno najvise prijava za posao; koristi se bucket agregation)

