/* const app = Vue.createApp({ data(){return{}},created(){},methods:{} }); app.mount("#app");*/
const app = Vue.createApp({
    data(){
        return {
            clientes:[],
            json:{},
            nombre:"",
            apellido:"",
            mail:""
        }
    },
    created(){
        axios.get("/rest/clients")
        .then(response => {
            this.loadData(response);
        })
        .catch(error => console.log(error.message))
    },
    methods:{
        loadData(r){
            this.clientes = r.data._embedded.clients;
            this.json = r.data;
        },
        addClient(){
            if(this.nombre && this.apellido && this.mail){
                this.postClient();
            }
        },
        postClient(){
            axios.post("/rest/clients", {
                firstName: this.nombre,
                lastName: this.apellido,
                email: this.mail
            })
            .then(response => {
                console.log(response.data._embedded.clients)
                loadData(response);
            })
            .catch(error => console.log(error.message))
        }
    }
})
app.mount("#app")