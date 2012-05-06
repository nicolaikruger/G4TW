var Controller = (function() {
    var fromForm = document.getElementById('fromForm');
    var adr1ID = 0;
    var adr2ID = 0;
    var loader = document.getElementById("loading");

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
            url = encodeURI(url);

            var req = new XMLHttpRequest();

            req.onreadystatechange = callback;
            req.open("GET", url, callback);
            req.send(null);
        }
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

            console.log(xml);

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
            } catch (e) {};

            switch(errorType) {
                case 0:
                    window.alert("No errors!");
                    break;
                case 1:
                    // Could not find the address
                    handleError1();
                    break;
                case 2:
                    window.alert("Multiple hits!");
                    handleError2();
                    break;
            }

            function handleError1 () {
                var s = "The following address(es) didn't gave any result in the system:\n"
                var result = xml.evaluate("//address", xml, null, XPathResult.ANY_TYPE, null);
                var adr;
                s = s + "\n * " + result.iterateNext().childNodes[0].nodeValue;
                try {
                    s = s + "\n * " + result.iterateNext().childNodes[0].nodeValue;
                } catch(e) {};

                window.alert(s);
            }

            function handleError2 () {

            }

            // Initiate the view
            //View.draw();

            // Hide the loader.gif
            loader.style.display = "none";
        }
    }

    return {};
}());
