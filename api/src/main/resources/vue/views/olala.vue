<template id="olala">
  <app-frame>
    <div class="hello-world">
        olala2
    </div>
    <div>
      <a :href="`/purum`">Purum</a>
    </div>
    <div v-if="users">
      <ul class="user-overview-list">
        <li v-for="user in users">
          <a :href="`/users/${user.uid}`">{{user.username}}</a>
        </li>
      </ul>
    </div>
    <div v-else>
      no users
    </div>
  </app-frame>
</template>
<script>
  app.component("olala", {
    template: "#olala",
    data: () => ({
      users: null,
    }),
    created() {
      fetch("/api/users")
        .then(res => res.json())
        .then(res => this.users = res)
        .catch(() => alert("Error while fetching users"));
    }
  });
</script>
<style>
  .hello-world {
    color: goldenrod;
    padding-left: 20px;
    padding-right: 20px;
  }
</style>