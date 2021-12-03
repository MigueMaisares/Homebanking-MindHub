const app = Vue.createApp({
    data() {
        return {
            clientes: [],
            cards: [],
            cardsDebito: [],
            cardsCredito: []
        }
    },
    created() {
        axios.get("/api/clients/current")
            .then(response => {
                console.log(response)
                this.clientes = response.data
                this.name = this.clientes.firstName
                this.lname = this.clientes.lastName
                this.cards = response.data.cards
                this.cards.sort((a, b) => a.id - b.id)
                this.cardsDebito = this.cards.filter(e => e.type == 'DEBIT')
                this.cardsCredito = this.cards.filter(e => e.type == 'CREDIT')
            })
            .catch(error => console.log(error.message))
    },
    methods: {
        formatoFecha(fecha) {
            return fecha = fecha.split("").splice(0, 7).join("")
        },
        logout(){
            axios.post('/api/logout').then(() => window.location.href = "/web/index.html").catch(error => console.log(error.message))
        },
        deleteCard(id){
            Swal.fire({
                title: 'Are you sure?',
                text: "You will not be able to reverse the request",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, I want to remove the card'
            })
            .then((result) =>  {
                if (result.isConfirmed) {
                    axios.delete("/api/clients/current/cards?id=" + id)
                    .then(
                        Swal.fire({
                            title:'Made',
                            text:'Your card will be deleted immediately',
                            icon:'success',
                            showConfirmButton: false,
                        }),
                        setTimeout( () => window.location.href = "/web/cards.html",2000))
                    .catch(() => 
                        Swal.fire({
                            title: 'An error occurred',
                            showConfirmButton: false,
                            icon: "error",
                            showCloseButton: false,
                        })
                    )
                }
            })
        },
        esVencida(thru){
            const hasta = new Date(thru)
            const ahora = new Date()
            if (ahora >= hasta){
                return true
            } else {
                return false
            }
        }
    }
})
app.mount("#app")