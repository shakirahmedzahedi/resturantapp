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
  full: "/menu/full-menu.webp",
  combo1: "/menu/combo-1.webp",
  combo2: "/menu/combo-2.webp"
};

const menuItems: MenuItem[] = [
  { id: "fuchka", nameBn: "ফুচকা", nameEn: "Fuchka", price: 50, image: "/menu/items/fuchka.webp" },
  { id: "chotpoti", nameBn: "চটপটি", nameEn: "Chotpoti", price: 50, image: "/menu/items/chotpoti.webp" },
  { id: "doi-bora", nameBn: "দই বড়া", nameEn: "Doi Bora", price: 30, image: "/menu/items/doi-bora.webp" },
  { id: "jhal-muri", nameBn: "ঝাল মুড়ি", nameEn: "Jhal Muri", price: 30, image: "/menu/items/jhal-muri.webp" },
  { id: "boot-piyaju", nameBn: "বুট + পিয়াজু", nameEn: "Boot + Piyaju", price: 30, image: "/menu/items/boot-piyaju.webp" },
  { id: "singara", nameBn: "সিঙ্গারা", nameEn: "Singara", price: 30, image: "/menu/items/singara.webp" },
  { id: "roll", nameBn: "রোল", nameEn: "Roll", price: 30, image: "/menu/items/roll.webp" },
  { id: "chitoy", nameBn: "চিতই", nameEn: "Chitoi", price: 25, image: "/menu/items/chitoy.webp" },
  { id: "halim", nameBn: "হালিম", nameEn: "Halim", price: 50, image: "/menu/items/halim.webp" },
  { id: "tehari", nameBn: "তেহারি", nameEn: "Tehari", price: 100, image: "/menu/items/tehari.webp" },
  { id: "aam-vorta", nameBn: "আম ভর্তা", nameEn: "Aam Vorta", price: 30, image: "/menu/items/aam-vorta.webp" },
  { id: "jambura-vorta", nameBn: "জাম্বুরা ভর্তা", nameEn: "Jambura Vorta", price: 30, image: "/menu/items/jambura-vorta.webp" },
  { id: "achar", nameBn: "আচার", nameEn: "Achar", price: 60, image: "/menu/items/achar.webp" },
  { id: "white-misti", nameBn: "সাদা মিষ্টি", nameEn: "White Misti", price: 20, image: "/menu/items/white-misti.webp" },
  { id: "chomchom", nameBn: "চমচম", nameEn: "Chomchom", price: 25, image: "/menu/items/chomchom.webp" },
  { id: "kalojam", nameBn: "কালোজাম", nameEn: "Kalojam", price: 25, image: "/menu/items/kalojam.webp" },
  { id: "pudding", nameBn: "পুডিং", nameEn: "Pudding", price: 30, image: "/menu/items/pudding.webp" },
  { id: "lolly", nameBn: "ললি", nameEn: "Lolly", price: 20, image: "/menu/items/lolly.webp" },
  { id: "cha", nameBn: "চা", nameEn: "Cha", price: 30, image: "/menu/items/cha.webp" },
  { id: "coffee", nameBn: "কফি", nameEn: "Coffee", price: 25, image: "/menu/items/coffee.webp" },
  { id: "water", nameBn: "পানি", nameEn: "Water", price: 20, image: "/menu/items/water.webp" },
  { id: "coke", nameBn: "কোক", nameEn: "Coke", price: 20, image: "/menu/items/coke.webp" },
  { id: "mango-juice", nameBn: "ম্যাংগো জুস", nameEn: "Mango Juice", price: 15, image: "/menu/items/mango-juice.webp" },
  { id: "red-bull", nameBn: "রেড বুল", nameEn: "Red Bull", price: 40, image: "/menu/items/red-bull.webp" },
  { id: "popcorn", nameBn: "পপকর্ন", nameEn: "Popcorn", price: 30, image: "/menu/items/popcorn.webp" },
  { id: "macaron", nameBn: "ম্যাকারন প্যাকেট", nameEn: "Macaron Packet", price: 30, image: "/menu/items/macaron.webp" },
  { id: "hot-dog", nameBn: "হট ডগ", nameEn: "Hot Dog", price: 20, image: "/menu/items/hot-dog.webp" }
];

export default function MenuPage() {
  const [selectedItem, setSelectedItem] = useState<MenuItem | null>(null);

  return (
    <main className="public-menu-page">
      <header className="public-menu-header">
        <div>
          <p className="public-menu-kicker">FACE MEDIA PRESENTS</p>
          <h1>মিরাবাই</h1>
          <p>হেইলা দুইলা, হেইলা দরবার নাচায়</p>
        </div>

        <div className="public-menu-actions">
          <a href="#items">খাবার</a>
          <a href="#full-menu">সম্পূর্ণ মেনু</a>
          <a href="#specials">কম্বো</a>
          <Link to="/">লগইন</Link>
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
          <p>{menuItems.length} টি আইটেম</p>
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
          <p>নির্দিষ্ট খাবারের বড় ছবি</p>
        </div>

        <div className="public-menu-special-grid">
          <article className="public-menu-special-card">
            <img src={menuImages.combo1} alt="কম্বো ১ - ঝাল মুড়ি ও চা" />
          </article>
          <article className="public-menu-special-card">
            <img src={menuImages.combo2} alt="কম্বো ২ - মিষ্টি, সিঙ্গারা ও পানি" />
          </article>
        </div>
      </section>

      <footer className="public-menu-footer">
        <strong>মিরাবাই</strong>
        <span>মেনু দেখতে এই পেজটি সবার জন্য উন্মুক্ত</span>
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
