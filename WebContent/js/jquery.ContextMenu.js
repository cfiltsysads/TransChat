(function($)
{
   var defaultValues = {
      idMenu: null, // string (required). The id of the menu that has to be shown
      posX: null,   // number (optional). The X coordinate used to show the menu
      posY: null,   // number (optional). The Y coordinate used to show the menu
      bindLeftClick: false // boolean (optional). If the menu has to be shown also on mouse left button click
   };

   var methods =
   {
      init: function(options)
      {
         if (typeof options === "string") {
            options = {idMenu: options};
         }
         options = $.extend(true, {}, defaultValues, options);

         if (options.idMenu == null) {
            $.error("No menu specified");
            return;
         } else if ($("#" + options.idMenu) == null) {
            $.error("The menu specified does not exist");
            return;
         }

         // Hide all if the user left-click or right-click outside the elements specified
         $("html").on(
            "contextmenu click",
            function() {
               $("#" + options.idMenu).hide();
            }
         );

         this.on(
            "contextmenu " + (options.bindLeftClick ? " click": ""),
            function(event) {
               event.preventDefault();
               event.stopPropagation();

               var posX = (options.posX == null) ? event.pageX : options.posX;
               var posY = (options.posY == null) ? event.pageY : options.posY;
               $("#" + options.idMenu)
                  .css({
                     top: posY + "px",
                     left: posX + "px"
                  })
                  .show();
            }
         );
      }
   };

   $.fn.ContextMenu = function (method) {
      if (methods[method])
         return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
      else if (typeof method === "object" || typeof method === "string" || !method)
         return methods.init.apply(this, arguments);
      else
         $.error("Method " + method + " does not exist on jQuery.ContextMenu");
   };
})(jQuery);
