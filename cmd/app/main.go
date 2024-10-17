package main

func main() {

	// New("nihao").SetName("monster")
	a := New("nihao")
	a.SetName("monster")

}

func New(name string) *Dog {
	return &Dog{name}
}

type Dog struct {
	name string
}

func (d *Dog) SetName(n string) {
	d.name = n
}
