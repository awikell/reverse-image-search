(function() {

    var $uploadedImage = $('#uploadedImage');
    var $preloader = $("#preloader");
    var $searchButton = $('#searchByImage');
    var $results = $("#results");

    $("#imgInput").change(function(){
        readImage(this);
    });

    $results.on('click', 'img', (function(e) {
        var imageData = $(this).attr('src');
        $uploadedImage.attr('src', imageData);
        $("html, body").animate({ scrollTop: 0 }, "slow");
        searchForImages(imageData);
    }));

    $searchButton.click(function(e) {
        e.preventDefault();
        var imageData = $uploadedImage.attr('src');
        searchForImages(imageData);
    });



    function readImage(input) {

        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                if(e.target.result && e.target.result.length > 0) {
                    $uploadedImage.attr('src', e.target.result);
                    $uploadedImage.show();
                    $searchButton.removeAttr('disabled');

                }
            };
            reader.readAsDataURL(input.files[0]);
        }
    }

    function searchForImages(imageData) {
        $results.empty();
        $preloader.show();

        var requestData = {
            imageData: imageData,
            searchType: $("#searchType").val()
        };

        $.ajax({
            url: '/searchByImageData',
            type: 'POST',
            dataType: "json",
            accepts: {
                text: "application/json"
            },
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(requestData),
            success: function (data) {
                $.each(data, function(i, item) {
                    $results.append("<img src=\"" +data[i].imageData + "\"/>");
                });

            },
            error: function () {
                alert('Search failed');
            },
            complete: function() {
                $preloader.hide();
            }
        });
    }

})();

