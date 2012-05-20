var Controller = (function() {
    var selectRoadsForm = document.getElementById("selectRoads");
    var fromForm = document.getElementById('fromForm');
    var adr1ID = 0;
    var adr2ID = 0;
    var loader = document.getElementById("loading");
    var chooser = document.getElementById("name");

    selectRoadsForm.submit.onclick = function() {
        chooser.style.display = "none";

        var fromRoads = document.getElementsByName("fromRoad");
        var toRoads = document.getElementsByName("toRoad");

        for(var i = 0; i < fromRoads.length; i++) {
            if(fromRoads[i].checked) {
                alert(toRoads[i].value);
                adr1ID = fromRoads[i].value;
            }
        }

        for(var j = 0; j < toRoads.length; j++) {
            if(toRoads[j].checked) {
                alert(toRoads[j].value);
                adr2ID = toRoads[j].value;
            }
        }
        var url = "path?adr1=" + fromForm.from.value + "&adr2=" + fromForm.to.value + "&id1=" + adr1ID + "&id2=" + adr2ID;
        alert(url);
        getXML(url);
    };

    fromForm.submit.onclick = function() {
        if(fromForm.from.value.trim().length == 0 && fromForm.to.value.trim().length == 0)
            window.alert("There was no \"from\" or \"to\" addresses!");
        else if(fromForm.from.value.trim().length == 0)
            window.alert("There was no \"from\" address!");
        else if(fromForm.to.value.trim().length == 0)
            window.alert("There was no \"to\" address!");
        else {
            loader.style.display = "block";
            var url = "path?adr1=" + fromForm.from.value + "&adr2=" + fromForm.to.value + "&id1=" + adr1ID + "&id2=" + adr2ID;
            getXML(url);
        }
    };

    function getXML(url) {
        url = encodeURI(url);

        var req = new XMLHttpRequest();

        req.onreadystatechange = callback;
        req.open("GET", url, callback);
        req.send(null);
    }

    var callback = function(e) {
        // Initiate variables
        var xml;

        // Get the request-event
        var req = e.currentTarget;

        // Function to be called when result arrives
        if (req.readyState == 4 && (req.status == 0 || req.status == 200)) {

            // Get the DOMParser and parse the response-string
            var parser = new DOMParser();
            xml = parser.parseFromString(String(req.response), "text/xml");

            /**
             * The error type for a path request.
             * 0 = No errors, a path was found
             * 1 = One or both of the address(es) could not be found in the system
             * 2 = One or both of the address(es) gave multiple results.
             * @type {Number}
             */
            var errorType = 0;
            try {
                errorType = Number(xml.evaluate("error", xml, null, XPathResult.ANY_TYPE, null).iterateNext().attributes[0].value);
            } catch (e) {}

            switch(errorType) {
                case 0:
                    // No errors!
                    drawPath();
                    break;
                case 1:
                    // Could not find the address
                    handleError1();
                    loader.style.display = "none";
                    break;
                case 2:
                    // Multiple hits!
                    loader.style.display = "none";
                    chooser.style.display = "block";
                    handleError2();
                    break;
            }

            function drawPath () {
                Model.clearPath();

                var roadIterator = xml.evaluate("//r", xml, null, XPathResult.ANY_TYPE, null);
                var i = xml.evaluate("//i", xml, null, XPathResult.ANY_TYPE, null);
                var fx = xml.evaluate("//fx", xml, null, XPathResult.ANY_TYPE, null);
                var fy = xml.evaluate("//fy", xml, null, XPathResult.ANY_TYPE, null);
                var tx = xml.evaluate("//tx", xml, null, XPathResult.ANY_TYPE, null);
                var ty = xml.evaluate("//ty", xml, null, XPathResult.ANY_TYPE, null);
                var road = roadIterator.iterateNext();

                // Clears the path array
                Model.clearPath();

                // Iterate over possible matches
                while (road) {
                    /**
                     * Tries to parse a given input to a number.
                     * @param e  The element to parse.
                     */
                    function toNumber(e) {
                        return Number(e.iterateNext().childNodes[0].nodeValue);
                    }

                    // Retrieve values
                    var x1 = toNumber(fx);
                    var y1 = toNumber(fy);
                    var x2 = toNumber(tx);
                    var y2 = toNumber(ty);

                    // Add the road to the path array
                    Model.getRoads("path").push({from:Vector(x1, y1), to:Vector(x2, y2)});

                    road = roadIterator.iterateNext();
                }

                View.draw();
            }

            function handleError1 () {
                var s = "The following address(es) didn't gave any result in the system:\n";
                var result = xml.evaluate("//address", xml, null, XPathResult.ANY_TYPE, null);
                var adr;
                s = s + "\n * " + result.iterateNext().childNodes[0].nodeValue;
                try {
                    s = s + "\n * " + result.iterateNext().childNodes[0].nodeValue;
                } catch(e) {}

                window.alert(s);
            }

            function handleError2 () {

                document.getElementById("fromRoads").innerHTML = readXML(1, "fromRoad");
                document.getElementById("toRoads").innerHTML = readXML(2, "toRoad");

                function readXML(i, str) {
                    var HTML = "";

                    var roadIterator = xml.evaluate("error/collection["+i+"]/r", xml, null, XPathResult.ANY_TYPE, null);
                    var id = xml.evaluate("error/collection["+i+"]/r/id", xml, null, XPathResult.ANY_TYPE, null);
                    var n = xml.evaluate("error/collection["+i+"]/r/n", xml, null, XPathResult.ANY_TYPE, null);
                    var sn = xml.evaluate("error/collection["+i+"]/r/sn", xml, null, XPathResult.ANY_TYPE, null);
                    var en = xml.evaluate("error/collection["+i+"]/r/en", xml, null, XPathResult.ANY_TYPE, null);
                    var sl = xml.evaluate("error/collection["+i+"]/r/sl", xml, null, XPathResult.ANY_TYPE, null);
                    var el = xml.evaluate("error/collection["+i+"]/r/el", xml, null, XPathResult.ANY_TYPE, null);
                    var lpc = xml.evaluate("error/collection["+i+"]/r/lpc", xml, null, XPathResult.ANY_TYPE, null);
                    var rpc = xml.evaluate("error/collection["+i+"]/r/rpc", xml, null, XPathResult.ANY_TYPE, null);

                    var road = roadIterator.iterateNext();

                    // Iterate over possible matches
                    while (road) {
                        /**
                         * Tries to parse a given input to a number.
                         * @param e  The element to parse.
                         */
                        function read(e) {
                            return e.iterateNext().childNodes[0].nodeValue;
                        }

                        // Retrieve values
                        var roadID = read(id);
                        var name = read(n) + " ";
                        var startNumber = read(sn);
                        var endNumber = read(en);
                        var startLetter = read(sl);
                        var endLetter = read(el);
                        var leftPostal = read(lpc);
                        var rightPostal = read(rpc);


                        var numbers = "Street numbers: " + startNumber + " to " + endNumber + " ";
                        var letters = "Street Letters: " + startLetter + " to " + endLetter + " ";
                        var postalCodes = "Postal codes: " + leftPostal + " and " + rightPostal;

                        if(startNumber == "0" && endNumber == "0")
                            numbers = "No numbers. ";
                        else if(startNumber == endNumber)
                            numbers = "Street number: " + startNumber + " ";
                        else if(startNumber == "0")
                            numbers = "Street number: " + endNumber + " ";
                        else if(endNumber == "0")
                            numbers = "Street number: " + startNumber + " ";

                        if(startLetter.length == 1 && endLetter.length == 1)
                            letters = "";
                        else if (startLetter.length == 1 && endLetter.length != 1)
                            letters = "Street letter: " + endLetter + " ";
                        else if (startLetter.length != 1 && endLetter.length == 1)
                            letters = "Street letter: " + startLetter + " ";

                        if(leftPostal == rightPostal)
                            postalCodes = "Postal code: " + leftPostal;

                        HTML += "<input type='radio' name='" + str + "' id='" + roadID + "' value='" + roadID + "'><label for='" + roadID + "'>" + name + numbers + letters + postalCodes + "</label><br />";

                        road = roadIterator.iterateNext();
                    }

                    return HTML;
                }
            }
        }
    };

    return {};
}());
