const app = Vue.createApp({
    data(){
        return {
            clientes:[],
            cuentas:[],
            prestamos:[]
        }
    },
    created(){
        axios.get("/api/clients/current")
            .then(response => {
                console.log(response)
                this.clientes = response.data;
                this.prestamos = response.data.loans;
                this.cuentas = response.data.accounts;
                this.prestamos.sort((a,b) => a.id<b.id ? 1 : -1)//DESC
                this.cuentas.sort((a,b) => a.id>b.id ? 1 : -1)//ASC
            })
            .catch(error => console.log(error.message))
    },
    methods:{
        logout(){
            axios.post('/api/logout').then(() => window.location.href = "/web/index.html").catch(error => console.log(error.message))
        },
        formatoMoneda(dinero){    
            return new Intl.NumberFormat('es-AR', {style: 'currency',currency: 'USD', minimumFractionDigits: 2}).format(dinero);
        },
        formatoFecha(fecha) {
            return new Date(fecha).toLocaleDateString()
        }
    }
})
app.mount("#app")