#!/bin/sh

# import assembly
curl -s -XGET "http://localhost:8080/assembly/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/assembly.A.json" | jsonpp
curl -s -XGET "http://localhost:8080/assembly/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/assembly.B.json" | jsonpp

# import adapters
curl -s -XGET "http://localhost:8080/part/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/part.ADAPT-4.5VDC-1A.json" | jsonpp
curl -s -XGET "http://localhost:8080/part/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/part.ADAPT-12VDC-1A.json" | jsonpp
curl -s -XGET "http://localhost:8080/part/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/part.ADAPT-JACK-PLUG.json" | jsonpp

# import FLEX strips
curl -s -XGET "http://localhost:8080/part/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/part.FLEX-4.5VDC-3528.json" | jsonpp
curl -s -XGET "http://localhost:8080/part/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/part.FLEX-4.5VDC-3528-LINK.json" | jsonpp
curl -s -XGET "http://localhost:8080/part/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/part.FLEX-12VDC-3528.json" | jsonpp
curl -s -XGET "http://localhost:8080/part/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/part.FLEX-12VDC-3528-LINK.json" | jsonpp
curl -s -XGET "http://localhost:8080/part/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/part.FLEX-12VDC-5050.json" | jsonpp
curl -s -XGET "http://localhost:8080/part/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/part.FLEX-12VDC-5050-LINK.json" | jsonpp

# import Lead wires
curl -s -XGET "http://localhost:8080/part/import?pathURI=file:///Users/carl_downs/work/dev/ledex/data/part.WIRE-LEAD-1A.json" | jsonpp

# update catalog
curl -s -XGET "http://localhost:8080/assembly/update-catalog" | jsonpp

