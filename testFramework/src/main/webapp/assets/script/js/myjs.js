(function ($){
  // Back to top button
  $(window).scroll(function() {
    if ($(this).scrollTop() > 100) {
      $('.back-to-top').fadeIn('slow');
    } else {
      $('.back-to-top').fadeOut('slow');
    }
  });
  $('.back-to-top').click(function(){
    $('html, body').animate({scrollTop : 0},1500, 'easeInOutExpo');
    return false;
  });
})(jQuery);

$(window).trigger('scroll');
  $(window).bind('scroll', function () {
    var pixels = 50;
    var top = 1200;
    if ($(window).scrollTop() > pixels) {
      $('.contains').addClass('contains-reduce');
      $('.contains').removeClass('contains-trans');
    } else {
      $('.contains').addClass('contains-trans');
      $('.contains').removeClass('contains-reduce');
    }
    if ($(window).scrollTop() > top) {
      $('.scrolltop-mf').fadeIn(1000, "easeInOutExpo");
    } else {
      $('.scrolltop-mf').fadeOut(1000, "easeInOutExpo");
    }
  });

  $(window).scroll(
  {
    previousTop: 50
  },
  function () {
    var currentTop = $(window).scrollTop();
    if(currentTop > this.previousTop){
      $('.contains').addClass('contains-reduce');
      $('.contains').removeClass('contains-trans');
    }
    else{
      $('.contains').addClass('contains-trans');
      $('.contains').removeClass('contains-reduce');
    }
    this.previousTop = currentTop;
  });