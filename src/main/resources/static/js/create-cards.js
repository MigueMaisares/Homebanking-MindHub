const app = Vue.createApp({
    data(){
        return{
            cardColor:"",
            cardType:"",
            cardsDebito:[],
            cardsCredito:[],
            cliente:[]
        }
    },
    created(){
        axios.get("/api/clients/current")
        .then(response => {
            this.cardsDebito = response.data.cards.filter(e => e.type == 'DEBIT')
            console.log(this.cardsDebito)
            this.cardsCredito = response.data.cards.filter(e => e.type == 'CREDIT')
            console.log(this.cardsCredito)
            this.cliente = response.data
        })
        .catch(error => console.log(error.message))
    },
    methods:{
        logout(){
            axios.post('/api/logout').then(() => window.location.href = "/web/index.html").catch(error => console.log(error.message))
        },
        createCard(){
            axios.post("/api/clients/current/cards",
                "type=" + this.cardType + "&color=" + this.cardColor,
                { headers:{'content-type':'application/x-www-form-urlencoded' }}
            )
            .then(() => window.location.href = "/web/cards.html")
            .catch(error => console.log(error.message))
        },
        fechaDesde() {
            return new Date().toLocaleDateString().split("").splice(-7, 7).join("")
        },
        fechaHasta() {
            let hoy = new Date()
            return new Date(hoy.getFullYear()+5, hoy.getMonth(), hoy.getDate()).toLocaleDateString().split("").splice(-7, 7).join("")
        }
    }
})
app.mount("#app")