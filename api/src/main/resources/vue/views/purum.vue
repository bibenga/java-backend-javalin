<template id="purum">
  <app-frame>
    <div class="purum">
        purum
    </div>
    <div>
      <a :href="`/olala`">Olala</a>
    </div>
  </app-frame>
</template>
<script>
  app.component("purum", {
    template: "#purum"
  });
</script>
<style>
  .purum {
    color: goldenrod;
    padding-left: 20px;
    padding-right: 20px;
  }
</style>