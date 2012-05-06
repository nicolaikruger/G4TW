var Controller = (function() {
    var fromForm = document.getElementById('fromForm');
    var adr1ID = 0;
    var adr2ID = 0;

    fromForm.submit.onclick = function() {
        if(fromForm.from.value.trim().length == 0 && fromForm.to.value.trim().length == 0)
            window.alert("There was no \"from\" or \"to\" addresses!");
        else if(fromForm.from.value.trim().length == 0)
            window.alert("There was no \"from\" address!");
        else if(fromForm.to.value.trim().length == 0)
            window.alert("There was no \"to\" address!");
        else {
            var url = "path?adr1=" + fromForm.from.value + "&adr2=" + fromForm.to.value + "&id1=" + adr1ID + "&id2=" + adr2ID;
            url = encodeURI(url);

            var req = new XMLHttpRequest();

            window.alert(url);

            //req.onreadystatechange = callback;
            //req.open("GET", url, callback);
            //req.send(null);
        }
    }

    return {};
}());
