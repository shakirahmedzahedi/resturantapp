import { useState } from "react";
import { Link } from "react-router-dom";

type MenuItem = {
  id: string;
  nameBn: string;
  nameEn: string;
  price: number;
  image: string;
};

const menuImages = {
  full: "/menu/menu3.jpeg",
  combo1: "/menu/combo-1.webp",
  combo2: "/menu/combo2.png",
  tehari:"/menu/tehari1.jpeg"
};

const menuItems: MenuItem[] = [
  { id: "fuchka", nameBn: "ফুচকা", nameEn: "Fuchka", price: 50, image: "/menu/items/fuchka.jpeg" },
  { id: "chotpoti", nameBn: "চটপটি", nameEn: "Chotpoti", price: 50, image: "/menu/items/chotpoti.jpeg" },
  { id: "doi-bora", nameBn: "দই বড়া", nameEn: "Doi Bora", price: 40, image: "/menu/items/Doibora.jpeg" },
  { id: "jhal-muri", nameBn: "ঝাল মুড়ি", nameEn: "Jhal Muri", price: 30, image: "/menu/items/jalmuri.jpeg" },
  { id: "boot-piyaju", nameBn: "বুট + পিয়াজু", nameEn: "Boot + Piyaju", price: 30, image: "/menu/items/but.jpeg" },
  { id: "singara", nameBn: "সিঙ্গারা", nameEn: "Singara", price: 30, image: "/menu/items/singara.jpeg" },
  { id: "roll", nameBn: "রোল", nameEn: "Roll", price: 30, image: "/menu/items/role.jpeg" },
  { id: "chitoy", nameBn: "চিতই", nameEn: "Chitoi", price: 25, image: "/menu/items/chitoi.jpeg" },
  { id: "halim", nameBn: "হালিম", nameEn: "Halim", price: 60, image: "/menu/items/halim.jpeg" },
  { id: "tehari", nameBn: "তেহারি", nameEn: "Tehari", price: 100, image: "/menu/items/tehari.jpeg" },
  { id: "aam-vorta", nameBn: "আম ভর্তা", nameEn: "Aam Vorta", price: 30, image: "/menu/items/aamvorta.jpeg" },
  { id: "jambura-vorta", nameBn: "আমসত্ত্ব", nameEn: "Aam Sotto", price: 40, image: "/menu/items/aam sotto.jpeg" },
  { id: "achar", nameBn: "আচার", nameEn: "Achar", price: 80, image: "/menu/items/achar.jpeg" },
  { id: "white-misti", nameBn: "মালাই চপ", nameEn: "White Misti", price: 30, image: "/menu/items/whitemisty.jpeg" },
  { id: "chomchom", nameBn: "চমচম", nameEn: "Chomchom", price: 25, image: "/menu/items/chomchom.jpeg" },
  { id: "kalojam", nameBn: "গোলাপ জামুন", nameEn: "Kalojam", price: 25, image: "/menu/items/kalojam.jpeg" },
  { id: "pudding", nameBn: "পুডিং", nameEn: "Pudding", price: 30, image: "/menu/items/puding.jpeg" },
  { id: "candypack", nameBn: "ক্যান্ডি প্যাকেট", nameEn: "Candy Packet", price: 20, image: "/menu/items/candypack.png" },
  { id: "cha", nameBn: "চা", nameEn: "Cha", price: 30, image: "/menu/items/cha.jpeg" },
  { id: "coffee", nameBn: "কফি", nameEn: "Coffee", price: 25, image: "/menu/items/coffe.jpeg" },
  { id: "water", nameBn: "পানি", nameEn: "Water", price: 20, image: "/menu/items/water.webp" },
  { id: "coke", nameBn: "কোক", nameEn: "Coke", price: 20, image: "/menu/items/coke.webp" },
  { id: "mango-juice", nameBn: "জুস", nameEn: "Juice", price: 15, image: "/menu/items/mango-juice.webp" },
  { id: "red-bull", nameBn: "রেড বুল", nameEn: "Red Bull", price: 40, image: "/menu/items/red-bull.webp" },
  { id: "popcorn", nameBn: "পপকর্ন", nameEn: "Popcorn", price: 20, image: "/menu/items/popcorn.webp" },
  { id: "CupCake", nameBn: "কাপকেক", nameEn: "Cup Cake", price: 25, image: "/menu/items/cupcake.jpeg" },
  { id: "hot-dog", nameBn: "হট ডগ", nameEn: "Hot Dog", price: 20, image: "/menu/items/hot-dog.webp" },
  { id: "Chicken-wrap", nameBn: "চিকেন শর্মা", nameEn: "Chicken wrap", price: 60, image: "/menu/items/chicken-wrap.png" },
  { id: "chickenNuggets','চিকেন নাগেটস", nameBn: "চিকেন নাগেটস", nameEn: "Chicken Nuggets", price: 50, image: "/menu/items/chickenNuggets.jpeg" }
];

export default function MenuPage() {
  const [selectedItem, setSelectedItem] = useState<MenuItem | null>(null);

  return (
    <main className="public-menu-page">
      <header className="public-menu-header">
        <div>
          <h1>মিরাবাই</h1>
          <p>হেইলা দুইলা, হেইলা দরবার নাচায়</p>
        </div>

        <div className="public-menu-actions">
          <a href="#items">খাবার</a>
          <a href="#full-menu">সম্পূর্ণ মেনু</a>
          <a href="#specials">কম্বো</a>
        </div>
      </header>

      <section className="public-menu-intro">
        <span>আজকের মেনু</span>
        <h2>খাবার, নাস্তা, মিষ্টি ও পানীয়</h2>
        <p>প্রতিটি খাবারের আলাদা ছবি, নাম ও দাম নিচে দেওয়া আছে। ছবিতে চাপ দিলে বড় করে দেখা যাবে।</p>
      </section>

      <section id="items" className="public-menu-section">
        <div className="public-menu-section-heading">
          <div>
            <span>FOOD ITEMS</span>
            <h2>প্রতিটি খাবার</h2>
          </div>

        </div>

        <div className="public-menu-item-grid">
          {menuItems.map((item) => (
            <button
              key={item.id}
              type="button"
              className="public-menu-item-card"
              onClick={() => setSelectedItem(item)}
              aria-label={`${item.nameBn} বড় করে দেখুন`}
            >
              <div className="public-menu-item-image-wrap">
                <img src={item.image} alt={item.nameBn} loading="lazy" />
              </div>
              <div className="public-menu-item-info">
                <div>
                  <strong>{item.nameBn}</strong>
                  <span>{item.nameEn}</span>
                </div>
                <b>{item.price} SEK</b>
              </div>
            </button>
          ))}
        </div>
      </section>

      <section id="full-menu" className="public-menu-section">
        <div className="public-menu-section-heading">
          <div>
            <span>MENU POSTER</span>
            <h2>সম্পূর্ণ মেনু</h2>
          </div>
          <p>একসাথে পুরো মেনু দেখুন</p>
        </div>

        <a
          className="public-menu-poster-link"
          href={menuImages.full}
          target="_blank"
          rel="noreferrer"
          aria-label="সম্পূর্ণ মেনুর বড় ছবি খুলুন"
        >
          <img className="public-menu-poster" src={menuImages.full} alt="মিরাবাই সম্পূর্ণ খাবারের মেনু" />
        </a>
      </section>

      <section id="specials" className="public-menu-section public-menu-specials-section">
        <div className="public-menu-section-heading">
          <div>
            <span>SPECIAL</span>
            <h2>কম্বো অফার</h2>
          </div>

        </div>

        <div className="public-menu-special-grid">
          <article className="public-menu-special-card">
            <img src={menuImages.combo1} alt="কম্বো ১ - ঝাল মুড়ি ও চা" />
          </article>
          <article className="public-menu-special-card">
            <img src={menuImages.combo2} alt="কম্বো ২ - মিষ্টি, সিঙ্গারা ও পানি" />
          </article>
          <article className="public-menu-special-card">
             <img src={menuImages.tehari} alt="তেহারি ও পানি" />
           </article>
        </div>
      </section>

      <footer className="public-menu-footer">
        <strong>মিরাবাই</strong>

      </footer>

      {selectedItem && (
        <div
          className="public-menu-modal"
          role="dialog"
          aria-modal="true"
          aria-label={`${selectedItem.nameBn} বড় ছবি`}
          onClick={() => setSelectedItem(null)}
        >
          <div className="public-menu-modal-card" onClick={(event) => event.stopPropagation()}>
            <button
              type="button"
              className="public-menu-modal-close"
              onClick={() => setSelectedItem(null)}
              aria-label="বন্ধ করুন"
            >
              ×
            </button>
            <img src={selectedItem.image} alt={selectedItem.nameBn} />
            <div className="public-menu-modal-info">
              <div>
                <h3>{selectedItem.nameBn}</h3>
                <p>{selectedItem.nameEn}</p>
              </div>
              <strong>{selectedItem.price} SEK</strong>
            </div>
          </div>
        </div>
      )}
    </main>
  );
}
